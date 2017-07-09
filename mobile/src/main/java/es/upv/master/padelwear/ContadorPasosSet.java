package es.upv.master.padelwear;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static es.upv.master.padelwear.R.id.contador;

/**
 * Created by padres on 10/07/2017.
 */

public class ContadorPasosSet {

    private Integer contadorPasos;

    public void init(Integer value)
    {
        contadorPasos = value;
    }


    public Integer getContadorPasos(Integer value)
    {
        Integer res = value - contadorPasos;
        return res;
    }

}
