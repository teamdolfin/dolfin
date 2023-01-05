package edu.sungshin.dolfin;

public class Model {
    private String imguri;

    Model(){

    }

    public Model(String imguri){
        this.imguri=imguri;
    }
    public String getImguri(){
        return imguri;
    }
    public void setImguri(String imguri){
        this.imguri = imguri;
    }
}