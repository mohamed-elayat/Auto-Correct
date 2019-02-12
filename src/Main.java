
import java.io.IOException;

//Mohamed Elayat et Fatima Mostefai


//classe Main pour lancer et tester le programme.
//Ce programme suit l'organisation MVC
//Nous avons suivi le même modèle MVC que celui dans l'exemple suivant:
// http://www.leepoint.net/GUI/structure/40mvc.html
public class Main {

    public static void main(String[] args) throws IOException {

        Model m = new Model();
        View v = new View(  m  );
        Controller c = new Controller(  v, m  );

    }

}
