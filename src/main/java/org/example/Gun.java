package org.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@AllArgsConstructor
public class Gun {

    @Getter
    private final int max;
    @Getter
    private int ammo=5;

    public int reload(int ammo){
        if(ammo < 0) throw new IllegalArgumentException("Ammo less than 0");
        int diff = max-this.ammo;
        if(diff > ammo){
            this.ammo+=ammo;
            return 0;
        } else{
            this.ammo=max;
            return ammo-diff;
        }
    }

    public int unload(){
        int ammo = this.ammo;
        this.ammo = 0;
        return ammo;
    }

    public boolean isLoaded(){
        return ammo>0;
    }

    public void shoot(){
        if(ammo == 0) System.out.println("Клац");
        else {
            ammo--;
            System.out.println("Бах");
        }
    }

}
