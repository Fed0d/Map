package org.example.objects;

import com.bbn.openmap.omGraphics.OMArc;

import javax.persistence.*;

@Entity
@Table(name = "sectors")
public class Sector extends OMArc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "latitudeCenter")
    private double latitudeCenter;
    @Column(name = "longitudeCenter")
    private double longitudeCenter;
    @Column(name = "xRadius")
    private double xRadius;
    @Column(name = "yRadius")
    private double yRadius;
    @Column(name = "start")
    private double start;
    @Column(name = "extent")
    private double extent;
}
