package org.example.objects;

import com.bbn.openmap.omGraphics.OMPoly;

import javax.persistence.*;

@Entity
@Table(name = "polygons")
public class Polygon extends OMPoly {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "name")
    private String name;

}
