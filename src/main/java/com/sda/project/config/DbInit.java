package com.sda.project.config;

import com.sda.project.controller.exception.ResourceAlreadyExistsException;
import com.sda.project.model.Privilege;
import com.sda.project.model.PrivilegeType;
import com.sda.project.model.Project;
import com.sda.project.model.Role;
import com.sda.project.model.RoleType;
import com.sda.project.model.Sprint;
import com.sda.project.model.Task;
import com.sda.project.model.TaskStatus;
import com.sda.project.model.TaskType;
import com.sda.project.model.User;
import com.sda.project.repository.PrivilegeRepository;
import com.sda.project.repository.ProjectRepository;
import com.sda.project.repository.RoleRepository;
import com.sda.project.repository.SprintRepository;
import com.sda.project.repository.TaskRepository;
import com.sda.project.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DbInit {

    private static final Logger log = LoggerFactory.getLogger(DbInit.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SprintRepository sprintRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Bean
    public CommandLineRunner initialData() {
        return args -> {
            log.info("setup initial data");

            // create privileges
            Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeType.READ_PRIVILEGE);
            Privilege writePrivilege = createPrivilegeIfNotFound(PrivilegeType.WRITE_PRIVILEGE);

            // create roles
            createRoleIfNotFound(RoleType.ADMIN, Set.of(readPrivilege, writePrivilege));
            createRoleIfNotFound(RoleType.USER, Set.of(readPrivilege, writePrivilege));

            // create main admin, admin, user
            createAdmin();
            createUser();

            User user = userRepository.findByEmail("user@gmail.com").get();

            // create projects
            Project project1 = new Project();
            project1.setProjectKey("SDA");
            project1.setName("Software stuff");
            projectRepository.save(project1);

            Project project2 = new Project();
            project2.setProjectKey("Agile");
            project2.setName("Agile stuff");
            projectRepository.save(project2);

            // create sprint
            Sprint sprint1 = new Sprint();
            sprint1.setName("Sprint 1");
            sprint1.setDateFrom(LocalDate.now());
            sprint1.setDateTo(LocalDate.now().plusDays(14));
            sprint1.setProject(project1);
            sprintRepository.save(sprint1);

            Sprint sprint2 = new Sprint();
            sprint2.setName("Sprint 2");
            sprint2.setDateFrom(LocalDate.now());
            sprint2.setDateTo(LocalDate.now().plusDays(14));
            sprint2.setProject(project1);
            sprintRepository.save(sprint2);

            // create tasks
            Task task1 = new Task();
            task1.setProject(project1);
            task1.setSummary("summary");
            task1.setStoryPoints(5);
            task1.setDescription("description");
            task1.setStatus(TaskStatus.TODO);
            task1.setTaskType(TaskType.STORY);
            task1.setAssignee(user);
            task1.setSprint(sprint1);
            taskRepository.save(task1);

            Task task2 = new Task();
            task2.setProject(project1);
            task2.setSummary("summary");
            task2.setStoryPoints(5);
            task2.setDescription("description");
            task2.setStatus(TaskStatus.TODO);
            task2.setTaskType(TaskType.TASK);
            task2.setAssignee(user);
            task2.setSprint(sprint1);
            taskRepository.save(task2);
        };
    }

    private User createAdmin() {
        User admin = new User(
                "admin@gmail.com",
                "{bcrypt}$2y$12$92ZkDrGVS3W5ZJI.beRlEuyRCPrIRlkEHz6T.7MVmH38l4/VAHhyi",
                "bill",
                "clinton");
        Role adminRole = roleRepository.findByType(RoleType.ADMIN).orElseThrow();
        admin.addRole(adminRole);
        userRepository.save(admin);
        return admin;
    }

    private User createUser() {
        User user = new User(
                "user@gmail.com",
                "{bcrypt}$2y$12$92ZkDrGVS3W5ZJI.beRlEuyRCPrIRlkEHz6T.7MVmH38l4/VAHhyi",
                "alex",
                "vasile");
        Role userRole = roleRepository.findByType(RoleType.USER).orElseThrow();
        user.addRole(userRole);
        return userRepository.save(user);
    }

    @Transactional
    private Role createRoleIfNotFound(RoleType type, Set<Privilege> privileges) {
        return (Role) roleRepository.findByType(type)
                .map(existingPrivilege -> {
                    throw new ResourceAlreadyExistsException("role already exists");
                })
                .orElseGet(() -> {
                    Role role = new Role(type);
                    role.setPrivileges(privileges);
                    return roleRepository.save(role);
                });
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(PrivilegeType name) {
        return (Privilege) privilegeRepository.findByType(name)
                .map(existingPrivilege -> {
                    throw new ResourceAlreadyExistsException("privilege already exists");
                })
                .orElseGet(() -> privilegeRepository.save(new Privilege(name)));
    }
}
