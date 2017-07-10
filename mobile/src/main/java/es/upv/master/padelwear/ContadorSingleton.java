package es.upv.master.padelwear;

import static android.R.attr.value;

/**
 * Created by padres on 10/07/2017.
 */

public class ContadorSingleton {

    private Integer pasos;
    private Integer start;
    private static ContadorSingleton contadorSingleton;

    //Constructor
    private ContadorSingleton()
    {
        //this.contadorPasos = pasos;
    }

    public static ContadorSingleton getContadorSingletonInstance()
    {
        if (contadorSingleton == null) {
            contadorSingleton = new ContadorSingleton();
            contadorSingleton.start = 0;
            contadorSingleton.pasos = 0;
        }
        return contadorSingleton;
    }

    public void setPasos(Integer value)
    {
        pasos = value;
    }

    public void init(Integer value)
    {
        this.start = value;
        this.pasos = value;
    }

    public Integer getPasos()
    {
        return pasos - start;
    }

}
