package DbFiller.Entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(schema="\"Playbook\"", name="\"team\"")
public class Team {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="image")
    private String image;

    @OneToMany(mappedBy="team",cascade = CascadeType.ALL)
    List<PlayBook> playBooks;

    public Team() {
    }

    public Team(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Team(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PlayBook> getPlayBooks() {
        return playBooks;
    }

    public void setPlayBooks(List<PlayBook> playBook) {
        this.playBooks = playBook;
    }

    public void addPlayBooks(PlayBook ...playbooks){

        if(playBooks == null){
            playBooks= new ArrayList<>();
        }

        playBooks = Arrays.asList(playbooks);
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
