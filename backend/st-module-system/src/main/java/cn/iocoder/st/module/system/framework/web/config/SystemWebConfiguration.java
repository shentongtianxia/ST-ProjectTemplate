package cn.iocoder.st.module.system.framework.web.config;

import cn.iocoder.st.framework.swagger.config.StSwaggerAutoConfiguration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的 web 组件的 Configuration
 */
@Configuration(proxyBeanMethods = false)
public class SystemWebConfiguration {

    /**
     * system 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi systemGroupedOpenApi() {
        return StSwaggerAutoConfiguration.buildGroupedOpenApi("system");
    }

}
