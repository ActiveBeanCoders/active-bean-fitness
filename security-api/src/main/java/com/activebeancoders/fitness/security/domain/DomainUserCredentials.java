package com.activebeancoders.fitness.security.domain;

import com.activebeancoders.fitness.security.infrastructure.PasswordHasher;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

/**
 * @author Dan Barrese
 */
@Entity
@Table(name = "domain_user_credentials")
@NamedQueries({
        @NamedQuery(
                name = DomainUserCredentials.QUERY_FIND_BY_USERNAME,
                query = "select c from DomainUserCredentials c where c.username = :username"
        )
})
public class DomainUserCredentials implements Serializable {

    public static final String QUERY_FIND_BY_USERNAME = "findDomainUserCredentialsByUsername";

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "salt")
    private String salt;

    protected DomainUserCredentials() {
    }

    public DomainUserCredentials(@NotNull String username,
                                 @NotNull String plainTextPassword) {
        this.username = username;
        this.salt = UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString() + UUID.randomUUID().toString();
        this.passwordHash = PasswordHasher.encodePassword(plainTextPassword, this.salt);
    }

    public DomainUserCredentials(@NotNull String username,
                                 @NotNull String plainTextPassword,
                                 @NotNull String userSpecificSalt) {
        this.username = username;
        this.salt = userSpecificSalt;
        this.passwordHash = PasswordHasher.encodePassword(plainTextPassword, this.salt);
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getSalt() {
        return salt;
    }

}

