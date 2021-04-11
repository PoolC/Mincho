package org.poolc.api.batch.itemprocessor;

import org.poolc.api.batch.vo.project.ProjectDao;
import org.poolc.api.batch.vo.project.Projects;
import org.springframework.batch.item.ItemProcessor;

public class ProjectsItemProcessor implements ItemProcessor<Projects, ProjectDao> {
    @Override
    public ProjectDao process(final Projects projects) throws Exception {
        ProjectDao project = ProjectDao.of(projects);
        return project;
    }
}
