package com.activebeancoders.fitness.security.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Represents a person using our application/service.
 *
 * @author Dan Barrese
 */
@Entity
@Table(name = "domain_user")
@NamedQueries({
        @NamedQuery(
                name = DomainUser.QUERY_FIND_BY_USERNAME,
                query = "select u from DomainUser u where u.username = :username"
        )
})
public class DomainUser implements Serializable {

    public static final String QUERY_FIND_BY_USERNAME = "findDomainUserByUsername";

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "comma_separated_roles", nullable = false, length = 1000)
    private String commaSeparatedRoles;

    public DomainUser() {
    }

    // @formatter:off

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNickname() {
        return nickname == null ? username : nickname;
    }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getCommaSeparatedRoles() { return commaSeparatedRoles; }
    public void setSpaceDelimitedRoles(String commaSeparatedRoles) { this.commaSeparatedRoles = commaSeparatedRoles; }

    // @formatter:on

    @Override
    public String toString() {
        return username;
    }

}

