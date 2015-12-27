package com.tbd.tbd6.oferfas.Models;

import java.util.Date;

/**
 * Created by fireness on 27-12-15.
 */
public class Oferta {
    private int oferta_id;
    private int user_id;

    private Date date;

    private String title;
    private String description;
    private int price;

    //ubicacion
    private Double loc_lon;
    private Double loc_lat;
    //TODO:Revisar como están llegando las fotos y etiquetas.
    public Oferta(){
        this.setOferta_id(0);
        this.setUser_id(0);

        this.setDate(new Date());

        this.setTitle("Oferta de Ejemplo");
        this.setDescription("Descripcion mas larga de la oferta del ejemplo");
        this.setPrice(10000);

        this.setLoc_lon(0.0);
        this.setLoc_lat(0.0);
    }
    public Oferta(int id){
        //TODO:GET oferta desde servicio
        this.setOferta_id(id);
        this.setUser_id(0);

        this.setDate(new Date());

        this.setTitle("Oferta de Ejemplo desde REST");
        this.setDescription("Descripcion mas larga de la oferta del ejemplo");
        this.setPrice(10000);

        this.setLoc_lon(0.0);
        this.setLoc_lat(0.0);
    }


    public int getOferta_id() {
        return oferta_id;
    }

    public void setOferta_id(int oferta_id) {
        this.oferta_id = oferta_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Double getLoc_lon() {
        return loc_lon;
    }

    public void setLoc_lon(Double loc_lon) {
        this.loc_lon = loc_lon;
    }

    public Double getLoc_lat() {
        return loc_lat;
    }

    public void setLoc_lat(Double loc_lat) {
        this.loc_lat = loc_lat;
    }
}
