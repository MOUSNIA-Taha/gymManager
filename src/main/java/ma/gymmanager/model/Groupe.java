package ma.gymmanager.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.JoinColumn;


@Entity(name="groupes")

@Data
@NoArgsConstructor
public class Groupe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nom;
    private int nbplace;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Entraineur entraineur;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Sport sport;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "adherent_groupe", joinColumns = @JoinColumn(name = "groupe_id"), inverseJoinColumns = @JoinColumn(name = "adherent_id"))
    private List<Adherent> adherents;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_groupe",referencedColumnName = "id")
    private List<Planning> plannings;
}