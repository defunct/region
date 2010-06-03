package com.goodworkalan.region.mix;

import com.goodworkalan.mix.ProjectModule;
import com.goodworkalan.mix.builder.Builder;
import com.goodworkalan.mix.builder.JavaProject;

/**
 * Builds the project definition for Region.
 *
 * @author Alan Gutierrez
 */
public class RegionProject implements ProjectModule {
    /**
     * Build the project definition for Region.
     *
     * @param builder
     *          The project builder.
     */
    public void build(Builder builder) {
        builder
            .cookbook(JavaProject.class)
                .produces("com.github.bigeasy.region/region/0.1")
                .depends()
                    .development("org.mockito/mockito-core/1.6")
                    .development("org.testng/testng-jdk15/5.10")
                    .end()
                .end()
            .end();
    }
}
