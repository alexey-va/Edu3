package org.example;
public class Main {
    public static void main(String[] args) {

        //Building building = new Building(-1);

        Gun gun = new Gun(7,3);
        for(int i=0;i<5;i++){
            gun.shoot();
        }
        System.out.println("returned: "+gun.reload(8));
        gun.shoot();
        gun.shoot();
        gun.unload();
        gun.shoot();

    }



}