package DbFiller.Entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(schema="\"Playbook\"", name="\"playbook\"")
public class PlayBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name = "type")
    private String type;

    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name="coach")
    private Coach coach;
    @ManyToOne(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name="team")
    private Team team;

    @OneToMany(mappedBy = "playBook")
    private List<Formation> formations;


    public PlayBook() {
    }

    public PlayBook(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public PlayBook(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }


    public PlayBook(String name, String type, Team team) {
        this.name = name;
        this.type = type;
        this.team = team;
    }



    public PlayBook(Integer id, String name, String type, Team team) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.team = team;
    }

    public PlayBook(String name, String type, Coach coach, Team team) {
        this.name = name;
        this.type = type;
        this.coach = coach;
        this.team = team;
    }



    public PlayBook(Integer id, String name, String type, Coach coach, Team team) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coach = coach;
        this.team = team;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public List<Formation> getFormations() {
        return formations;
    }

    public void setFormations(List<Formation> schemes) {
        this.formations= formations;
    }

    public void addFormations(Formation ...formationsToAdd){

        if(formations == null){
            formations = new ArrayList<>();
        }

        formations = Arrays.asList(formationsToAdd);
    }

    @Override
    public String toString() {
        return "PlayBook{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", coach=" + coach +
                ", team=" + team +
                '}';
    }
}
