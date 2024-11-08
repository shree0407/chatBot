package dev.shreeya;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<App_Users, Long> {
    App_Users findByName(String name);
}
