package DbFiller.Entity;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(schema="\"Playbook\"", name="\"Coach\"")
public class Coach {

    private enum CoachType{HEAD, ASSISTANT, OFFENSE, DEFENSE,SPECIAL_TEAMS, }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="type")
    private CoachType type;

    @Column(name="image")
    private String imagePath;
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY,cascade =CascadeType.MERGE)
    List<PlayBook> playBooks;

    public Coach() {
    }

    public Coach(String name, CoachType type) {
        this.name = name;
        this.type = type;
    }



    public Coach(String name, CoachType type, String imagePath) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
    }

    public Coach(Integer id, String name, CoachType type,String imagePath) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
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

    public CoachType getType() {
        return type;
    }

    public void setType(CoachType type) {
        this.type = type;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<PlayBook> getPlayBooks() {
        return playBooks;
    }

    public void setPlayBooks(List<PlayBook> playBooks) {
        this.playBooks = playBooks;
    }

    public void addPlayBooks(PlayBook ...playBooksToAdd){

        playBooks = Arrays.asList(playBooksToAdd);
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}

