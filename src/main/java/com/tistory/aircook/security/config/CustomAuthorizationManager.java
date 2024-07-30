package com.tistory.aircook.security.config;

import com.tistory.aircook.security.config.annoation.RequiresPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 인가 관리자
 * 인가여부를 관리한다. 다음 {@link AuthorizeHttpRequestsConfigurer.AuthorizedUrl#access(AuthorizationManager) 메소드}의 인자로 적용
 *
 * @see AuthorizeHttpRequestsConfigurer.AuthorizedUrl#access(AuthorizationManager)
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    // 선언되어 있는 전체 request mapping 정보를 가져오기 위해
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * {@link AuthorizationManager} 인터페이스 메소드 override
     *
     * @param authentication              the {@link Supplier} of the {@link Authentication} to check
     * @param requestAuthorizationContext the {@link T} object to check
     * @return 인증결과
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {

        /*
        log.debug("authentication name is [{}]", authentication.get().getName());
        log.debug("authentication principal is [{}]", authentication.get().getPrincipal());
        log.debug("authentication credentials is [{}]", authentication.get().getCredentials());

        log.debug("request method is [{}]", requestAuthorizationContext.getRequest().getMethod());
        log.debug("request uri is [{}]", requestAuthorizationContext.getRequest().getRequestURI());
         */

        // 여기에 실제 권한 확인 로직을 구현합니다
        // 예: 데이터베이스 조회, 외부 서비스 호출 등
        String requestUrl = requestAuthorizationContext.getRequest().getRequestURI();
        String httpMethod = requestAuthorizationContext.getRequest().getMethod();

        // 허용되는 권한의 종류, annotation으로 정의함
        // 조건은 현재사용자 authentication.get().getName()가 가지고 있는 권한의 이름을 가져와서 List에 담는다.
        // Database에서 값을 가져오겠지?
        List<String> userPermissions = List.of("access:test01:read", "access:test01:create");

        // 요청된 엔드포인트의 필요 권한 확인
        String requiredPermission = getRequiredPermission(requestUrl, httpMethod);
        boolean isAccessAllowed = userPermissions.contains(requiredPermission);

        return new AuthorizationDecision(isAccessAllowed);
    }

    /**
     * @param requestUrl 요청 Http URL
     * @param httpMethod 요청 Http Method
     * @return 매칭된 {@link RequiresPermission}의 값
     */
    private String getRequiredPermission(String requestUrl, String httpMethod) {
        // 모든 핸들러 메소드를 순회하며 매칭되는 엔드포인트 찾기
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : requestMappingHandlerMapping.getHandlerMethods().entrySet()) {
            RequestMappingInfo mapping = entry.getKey();
            HandlerMethod method = entry.getValue();
            // URL 패턴 매칭
            boolean urlMatches = mapping.getPathPatternsCondition() != null && mapping.getPathPatternsCondition().getPatterns().stream().anyMatch(pattern -> pattern.matches(PathContainer.parsePath(requestUrl)));

            // HTTP 메소드 매칭
            boolean methodMatches = mapping.getMethodsCondition().getMethods().contains(RequestMethod.valueOf(httpMethod));

            // URL과 Method가 일치하면 method
            if (urlMatches && methodMatches) {
                RequiresPermission annotation = method.getMethodAnnotation(RequiresPermission.class);
                if (annotation != null) {
                    log.debug("RequiresPermission annotation is [{}]", annotation.value());
                    return annotation.value();
                }
            }
        }
        return "";
    }

}

