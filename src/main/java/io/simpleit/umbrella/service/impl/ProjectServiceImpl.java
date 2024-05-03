package io.simpleit.umbrella.service.impl;

import io.simpleit.umbrella.domain.Project;
import io.simpleit.umbrella.repository.ProjectRepository;
import io.simpleit.umbrella.service.ProjectService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link io.simpleit.umbrella.domain.Project}.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Project save(Project project) {
        log.debug("Request to save Project : {}", project);
        return projectRepository.save(project);
    }

    @Override
    public Project update(Project project) {
        log.debug("Request to update Project : {}", project);
        return projectRepository.save(project);
    }

    @Override
    public Optional<Project> partialUpdate(Project project) {
        log.debug("Request to partially update Project : {}", project);

        return projectRepository
            .findById(project.getId())
            .map(existingProject -> {
                if (project.getType() != null) {
                    existingProject.setType(project.getType());
                }
                if (project.getState() != null) {
                    existingProject.setState(project.getState());
                }
                if (project.getTitle() != null) {
                    existingProject.setTitle(project.getTitle());
                }
                if (project.getDescription() != null) {
                    existingProject.setDescription(project.getDescription());
                }
                if (project.getDailyRate() != null) {
                    existingProject.setDailyRate(project.getDailyRate());
                }
                if (project.getStartDate() != null) {
                    existingProject.setStartDate(project.getStartDate());
                }
                if (project.getEndDate() != null) {
                    existingProject.setEndDate(project.getEndDate());
                }
                if (project.getRemoteWorkPct() != null) {
                    existingProject.setRemoteWorkPct(project.getRemoteWorkPct());
                }

                return existingProject;
            })
            .map(projectRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        log.debug("Request to get all Projects");
        return projectRepository.findAll(pageable);
    }

    /**
     *  Get all the projects where TimeSheet is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Project> findAllWhereTimeSheetIsNull() {
        log.debug("Request to get all projects where TimeSheet is null");
        return StreamSupport.stream(projectRepository.findAll().spliterator(), false)
            .filter(project -> project.getTimeSheet() == null)
            .toList();
    }

    /**
     *  Get all the projects where Expense is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Project> findAllWhereExpenseIsNull() {
        log.debug("Request to get all projects where Expense is null");
        return StreamSupport.stream(projectRepository.findAll().spliterator(), false)
            .filter(project -> project.getExpense() == null)
            .toList();
    }

    /**
     *  Get all the projects where Invoice is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Project> findAllWhereInvoiceIsNull() {
        log.debug("Request to get all projects where Invoice is null");
        return StreamSupport.stream(projectRepository.findAll().spliterator(), false)
            .filter(project -> project.getInvoice() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Project> findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        return projectRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Project : {}", id);
        projectRepository.deleteById(id);
    }
}
