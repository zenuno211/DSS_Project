package armazem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapaCorredores {

    private Map<String, Map<String, Integer>> mapaCorredores;

    /**
     * Construtor que inicializa o mapa prédefenido
     */
    public MapaCorredores() {
        this.mapaCorredores = new HashMap<String, Map<String, Integer>>();

        Map<String, Integer> mapaC1 = new HashMap<>();

        mapaC1.put("C02", 5);
        mapaC1.put("ZE01", 12); // assumida distancia ao meio do corredor
        mapaC1.put("ZR01", 12); // assumida distancia ao meio do corredor

        Map<String, Integer> mapaC2 = new HashMap<>();
        mapaC2.put("C01", 5);
        mapaC2.put("ZE01", 17); // assumida distancia ao meio do corredor
        mapaC2.put("ZR01", 12); // assumida distancia ao meio do corredor

        this.mapaCorredores.put("C01", mapaC1);
        this.mapaCorredores.put("C02", mapaC2);

        Map<String, Integer> mapaZR = new HashMap<>();

        mapaZR.put("C01", 12); // assumida distancia ao meio do corredor
        mapaZR.put("C02", 17); // assumida distancia ao meio do corredor

        Map<String, Integer> mapaZE = new HashMap<>();
        mapaZE.put("C01", 12); // assumida distancia ao meio do corredor
        mapaZE.put("C02", 12); // assumida distancia ao meio do corredor

        this.mapaCorredores.put("ZR01", mapaZR);
        this.mapaCorredores.put("ZE01", mapaZE);
    }

    /**
     * Dado um código de corredor, retorna a lista de codigos de corredores adjacentes ao corredor dado
     */
    public Set<String> getAdjacentesA(String corredor) {
        return this.mapaCorredores.get(corredor).keySet();
    }

    // codCorredor -> codCorredor adjacente, distância
    // private Map<String, Map<String, Integer>> mapaCorredores;

    /**
     * Dado um percurso, calcula a sua distancia
     */
    public int distanciaPercurso(List<String> percurso) { // Percurso PratA -> A -> B -> C -> D -> PratB
        int res = 0;

        for (int i = 0; i < percurso.size() - 1; i++) {
            res += this.mapaCorredores.get(percurso.get(i)).get(percurso.get(i + 1));
        }

        return res;
    }

    /**
     * Calcula a distancia entre dois corredores
     */
    public Integer distanciaEntreCorredores(List<String> corredoresPercorridos, String corredorInicial, String corredorFinal) {
        if (corredoresPercorridos.contains(corredorInicial)) {
            return 20001; //
        } else if (this.mapaCorredores.get(corredorInicial).containsKey(corredorFinal)) {
            return this.mapaCorredores.get(corredorInicial).get(corredorFinal);
        } else {
            List<Integer> distancias = new ArrayList<Integer>();
            corredoresPercorridos.add(corredorInicial);
            for (String start : this.mapaCorredores.get(corredorInicial).keySet()) {
                distancias.add(this.mapaCorredores.get(corredorInicial).get(start)
                        + distanciaEntreCorredores(corredoresPercorridos, start, corredorFinal));
            }
            return Collections.min(distancias);
        }
    }

    /**
     * Determina o caminho entre dois corredores dado o percuso a percorrer entre eles 
     */
    public List<String> getCaminhoEntre(List<String> percurso, String corredorInicial, String corredorFinal) {
        String best = null;
        int shortestPath = 20000;
        if (percurso.contains(corredorInicial)) {
            return null;
        } else if (this.mapaCorredores.get(corredorInicial).containsKey(corredorFinal)) {
            percurso.add(corredorInicial);
            percurso.add(corredorFinal);
            return percurso;
        } else {
            percurso.add(corredorInicial);
            for (String start : this.mapaCorredores.get(corredorInicial).keySet()) {
                int distancia = distanciaEntreCorredores(percurso, start, corredorFinal);
                if (distancia < shortestPath) {
                    shortestPath = distancia;
                    best = start;
                }
            }

            return getCaminhoEntre(percurso, best, corredorFinal);
        }

    }

}