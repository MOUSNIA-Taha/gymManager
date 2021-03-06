package ma.gymmanager.model;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "abonnements")
@Data
@NoArgsConstructor
public class Abonnement {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;
    private LocalDate dateDebut;
    private int nbMois;
    private LocalDate datePaiment;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinColumn(name="fk_adherent",referencedColumnName="id",nullable=false)
    private Adherent adherent;
    
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinColumn(name="fk_sport",referencedColumnName="id",nullable=false)
    private Sport sport;
}