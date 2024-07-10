/**
 * © Panov Vitaly 2024 - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.github.ickee953.micros.pictures.repository;

import com.github.ickee953.micros.pictures.entity.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, UUID> {

    Optional<AppUser> findByUsername(String username);

}
