package io.jay.app;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class SpringBootFlywaySampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootFlywaySampleApplication.class, args);
    }

}

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "team")
class TeamEntity {
    @Id
    private String id;
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    private List<MemberEntity> members = new ArrayList<>();

    public TeamEntity(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "member")
class MemberEntity {
    @Id
    private String id;
    private String name;
    private String email;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;

    @Builder
    public MemberEntity(String name, String email, TeamEntity team) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.team = team;
    }
}

interface TeamRepository extends JpaRepository<TeamEntity, String> {}

interface MemberRepository extends JpaRepository<MemberEntity, String> {}