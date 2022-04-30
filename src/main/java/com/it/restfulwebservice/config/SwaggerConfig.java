package com.it.restfulwebservice.config;



import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.util.*;

//설정관련 , swaager에서 document(문서)를 만들어준다,
@Configuration
@EnableSwagger2 // 스웨거용도
public class SwaggerConfig {

    /**
     * api관련 문서를 만들어서 반환하여 사용자가 쉽게 볼수있도록 한다.
     * http://localhost:8088/v2/api-docs  (json 형태)
     * http://localhost:8088/swagger-ui/index.html   ( 문서 형태)
     * 아래 상수로 만드는 이유는 한번 고정이 되면 변경 할 필요가 없는 정보들이기떄문에
     */

    // 연락처정보
    private  static  final Contact DEFAULT_CONTACT = new Contact(
            "HWANG MIN SANG"
            ,"http://www.naver.com"
            ,"alstkd1q1q@naver.com");
    // api 정보
    private static final ApiInfo DEFAULT_API_INFO =new ApiInfo(
            "Awesome API Title"
            ,"My User management REST API service"
            ,"1.0"
            ,"urn:tos"
            ,DEFAULT_CONTACT
            ,"Apache 2.0"
            ,"htttp://www.apache.org/licenses/LICENSE-2.0"
            ,new ArrayList<>());


    //문서타입형식
    //asList메서드는 배열형태로 바꿔줌
   private  static  final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(
            Arrays.asList("application/json","application/xml"));


   //swagger 문서 만들기위해 필요
    @Bean
    public Docket api(){

        //커스텀마이징했음.
        //초기는 new Docket(DocumentationType.SWAGGER_2)
         return new Docket(DocumentationType.SWAGGER_2)
                 .apiInfo(DEFAULT_API_INFO)
                 .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                 .consumes(DEFAULT_PRODUCES_AND_CONSUMES);

    }

    /**
     *
     * @param webEndpointsSupplier
     * @param servletEndpointsSupplier
     * @param controllerEndpointsSupplier
     * @param endpointMediaTypes
     * @param corsProperties
     * @param webEndpointProperties
     * @param environment
     * @return
     *  actuator 모니터링 설정
     *  http://localhost:8088/actuator  //전체보기
     *   http://localhost:8088/actuator/health  status가 up이면 서버가 가동중
     */

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }


    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment, String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath) || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}
