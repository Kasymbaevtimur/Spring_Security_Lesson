package peaksoft.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peaksoft.model.Role;
import peaksoft.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class UserService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void save(User user) {

        if (findAll().isEmpty()) {
            Role adminRole = roleService.findByName("ADMIN");
            adminRole.setUsers(Collections.singletonList(user));
            user.setRoles(Collections.singletonList(adminRole));
        } else {
            Role userRole = roleService.findByName("USER");
            userRole.setUsers(Collections.singletonList(user));
            user.setRoles(Collections.singletonList(userRole));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        entityManager.persist(user);
    }

    public User findByEmail(String email) {
        return entityManager.createQuery("select u from User u where u.email=:emaill", User.class)
                .setParameter("emaill", email).getSingleResult();
    }

    public List<User> findAll() {
        return entityManager.createQuery("from User", User.class).getResultList();

    }
    public void deleteById(Long id){
        entityManager.remove(entityManager.find(User.class,id));
    }


}
