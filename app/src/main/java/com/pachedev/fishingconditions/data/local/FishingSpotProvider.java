package com.pachedev.fishingconditions.data.local;

import com.pachedev.fishingconditions.model.domain.FishingSpot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides predefined fishing spots for the app.
 * Ordered clockwise around Tenerife, starting from Santa Cruz / Anaga.
 */
public class FishingSpotProvider {

    private FishingSpotProvider() {
        // Prevent instantiation
    }

    public static List<FishingSpot> getDefaultSpots() {
        return new ArrayList<>(Arrays.asList(
                new FishingSpot("Igueste de San Andrés", 28.522992, -16.151637),
                new FishingSpot("Las Gaviotas", 28.513022, -16.175424),
                new FishingSpot("Valleseco", 28.503791, -16.191351),
                new FishingSpot("Las Teresitas", 28.508420, -16.184099),
                new FishingSpot("Añaza", 28.420002, -16.294848),
                new FishingSpot("Radazul", 28.401293, -16.326099),
                new FishingSpot("Candelaria", 28.356340, -16.367717),
                new FishingSpot("El Socorro (Güimar)", 28.328645, -16.362675),
                new FishingSpot("Chimaje", 28.246051, -16.400150),
                new FishingSpot("Los Roques", 28.218366, -16.414679),
                new FishingSpot("Las Eras (Honduras)", 28.202356, -16.424234),
                new FishingSpot("Las Eras (Playa Honda)", 28.193500, -16.422854),
                new FishingSpot("Las Eras (punta)", 28.189198, -16.426565),
                new FishingSpot("El Porís (muelle)", 28.164314, -16.428503),
                new FishingSpot("El Porís (Playa Grande)", 28.153649, -16.430723),
                new FishingSpot("Abades (Cueva de Arena)", 28.141626, -16.432695),
                new FishingSpot("Abades (Abriguitos)", 28.142673, -16.438543),
                new FishingSpot("Abades (María Luisa)", 28.137306, -16.441797),
                new FishingSpot("La Listada", 28.123699, -16.457152),
                new FishingSpot("Caleta del Sordo", 28.112979, -16.463375),
                new FishingSpot("Tajao (muelle)", 28.108493, -16.469112),
                new FishingSpot("Roque Sardina", 28.104043, -16.472698),
                new FishingSpot("El Río", 28.094952, -16.481902),
                new FishingSpot("Los Tarajales", 28.088864, -16.490300),
                new FishingSpot("Playa del Medio", 28.076261, -16.502951),
                new FishingSpot("Cuevas del Trigo", 28.064937, -16.511684),
                new FishingSpot("Arenas del Mar", 28.049367, -16.528595),
                new FishingSpot("El Médano", 28.043569, -16.538803),
                new FishingSpot("Bocinegro", 28.033081, -16.539191),
                new FishingSpot("La Tejita", 28.029343, -16.552169),
                new FishingSpot("El Confital", 28.029241, -16.565382),
                new FishingSpot("La Mareta", 28.030722, -16.573647),
                new FishingSpot("Los Abrigos (piscinas)", 28.028937, -16.582307),
                new FishingSpot("Los Abrigos (Playa)", 28.027690, -16.596515),
                new FishingSpot("Golf del Sur", 28.023011, -16.608300),
                new FishingSpot("Colmenares", 28.018609, -16.624262),
                new FishingSpot("Punta los Alemanes", 28.013984, -16.629278),
                new FishingSpot("Amarilla", 28.006458, -16.638211),
                new FishingSpot("Costa del Silencio", 28.004432, -16.647063),
                new FishingSpot("Las Galletas (muelle)", 28.006489, -16.659317),
                new FishingSpot("El Banco", 28.004297, -16.671293),
                new FishingSpot("Faro La Rasca", 28.000355, -16.695209),
                new FishingSpot("Cala Mimar", 28.003960, -16.701543),
                new FishingSpot("Cala Mallorquín", 28.008054, -16.705626),
                new FishingSpot("El Callao (Palm Mar)", 28.015516, -16.706459),
                new FishingSpot("Palm Mar", 28.024500, -16.705500),
                new FishingSpot("La Arenita", 28.026571, -16.704611),
                new FishingSpot("Los Cristianos (muellito)", 28.043837, -16.711723),
                new FishingSpot("Los Cristianos (playa)", 28.049840, -16.717690),
                new FishingSpot("Las Vistas", 28.050116, -16.722309),
                new FishingSpot("Troya", 28.066460, -16.734594),
                new FishingSpot("Puerto Colón", 28.077263, -16.737821),
                new FishingSpot("Playa Fañabé", 28.085941, -16.737851),
                new FishingSpot("El Duque", 28.091177, -16.746939),
                new FishingSpot("La Enramada", 28.095739, -16.751293),
                new FishingSpot("La Caleta", 28.093100, -16.755200),
                new FishingSpot("Los Morteros", 28.105074, -16.758531),
                new FishingSpot("Punta Las Gaviotas", 28.104702, -16.761774),
                new FishingSpot("Puertito Adeje", 28.112729, -16.772822),
                new FishingSpot("Roca Negra", 28.117270, -16.776464),
                new FishingSpot("Callao Salvaje", 28.127704, -16.784125),
                new FishingSpot("El Balito", 28.138969, -16.791676),
                new FishingSpot("Playa San Juan", 28.178517, -16.811588),
                new FishingSpot("Alcalá (muelle)", 28.200990, -16.829701),
                new FishingSpot("Alcalá (La Jaquita)", 28.206278, -16.835086),
                new FishingSpot("Alcalá (punta)", 28.212111, -16.838513),
                new FishingSpot("Playa La Arena", 28.229586, -16.841607),
                new FishingSpot("Isla Cangrejo", 28.241475, -16.843801),
                new FishingSpot("Los Gigantes", 28.248241, -16.840369),
                new FishingSpot("Punta de Teno", 28.341570, -16.926102),
                new FishingSpot("Buenavista", 28.375416, -16.871170),
                new FishingSpot("Faro de La Laja", 28.391767, -16.837240),
                new FishingSpot("Los Chochos", 28.380481, -16.814022),
                new FishingSpot("Garachico (playa)", 28.371393, -16.768438),
                new FishingSpot("Garachico (puerto)", 28.374903, -16.753986),
                new FishingSpot("San Marcos", 28.378884, -16.725000),
                new FishingSpot("San Juan de la Rambla", 28.395762, -16.649614),
                new FishingSpot("Playa Las Aguas", 28.394625, -16.636498),
                new FishingSpot("Playa de Castro", 28.397877, -16.592545),
                new FishingSpot("Playa María Jiménez", 28.411258, -16.563282),
                new FishingSpot("Puerto de la Cruz", 28.419448, -16.550312),
                new FishingSpot("San Telmo (zona pesca)", 28.418426, -16.547907),
                new FishingSpot("Punta de Martiánez", 28.420128, -16.540847),
                new FishingSpot("El Bollullo", 28.418128, -16.519962),
                new FishingSpot("Los Patos", 28.421266, -16.509971),
                new FishingSpot("Rojas", 28.470804, -16.453145),
                new FishingSpot("El Puertito", 28.473370, -16.449864),
                new FishingSpot("Mesa del Mar", 28.500537, -16.424609),
                new FishingSpot("El Pris", 28.509799, -16.421608),
                new FishingSpot("La Barranquera", 28.537182, -16.398179),
                new FishingSpot("Jóver", 28.547232, -16.369330),
                new FishingSpot("Bajamar (Tenerife)", 28.557475, -16.343880),
                new FishingSpot("Punta del Hidalgo", 28.576245, -16.329939),
                new FishingSpot("Los Troches", 28.571482, -16.311433),
                new FishingSpot("Playa de Benijo", 28.572954, -16.192898),
                new FishingSpot("Playa del Draguillo", 28.582127, -16.177879),
                new FishingSpot("Roque Bermejo", 28.579978, -16.134428)
        ));
    }
}
