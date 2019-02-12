
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

//Mohamed Elayat et Fatima Mostefai

//class Model qui contient les methodes principales et la logique du programme.

public class Model {

    public  HashSet<String> dictionnaire;
    public String filePath;
    public ArrayList hilited = new ArrayList();


    public Model(){}

    public Model(  String filepath  ) throws IOException{

        this.filePath = filepath;
        this.dictionnaire = new HashSet<>();
        toHashSet(filePath);

    }

    //methode qui lit un fichier dictionnaire et met chaque
    //mot dans une structure de donnees HashSet
    public void toHashSet(  String filepath  ) throws IOException{
        String[] arr = lireFichier(  filepath  );

        for(  int i = 0; i < arr.length; i++ ){
            dictionnaire.add(  arr[i]  );
        }
    }

    //Methode qui prend un texte en paramettre et
    //retourne un tableau de String[] contenant les mots
    //individuels
    public String[] createTokens(String texte) {

        String[] texteToken;
        texte = texte.toLowerCase();
        texte = texte.replace(",", " ").replace(  ".", " ");
        texteToken = texte.split(" +");
        return texteToken;

    }

    //Methode qui prend un path en parametre. Elle va chercher et
    // tokenizer le contenu du texte, et le retourner.
    public String[] lireFichier(String filepath) throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        String line;
        String texte = "";

        while((line = reader.readLine()) != null) {
            texte += line + " ";
        }
        reader.close();

        String[] texteTokens = createTokens(texte);
        return texteTokens;
    }

    //Methode pour traverser le HashSet
    public Iterator<String> iterator() {
        return dictionnaire.iterator();
    }

    //Methode qui prend un mot et un dictionnaire
    //le mot n'est pas un mot dans le dictionnaire
    //et retourne les mots les plus proches qui
    //sont dans le dictionnaire
    //compare chaque mot dans le dictionnaire au mot inconu
    //et retient les 5 mots avec la distance la plus courte
    public String[] similarWords(String word, Model dict) {

        Iterator<String> word_iterator = dict.iterator();
        String[] best_words = new String[5];
        //initial value to fill the array
        int[] shortest_distances = {100000, 100000, 100000, 100000, 100000};

        while(word_iterator.hasNext()) {
            String nextWord = word_iterator.next();

            int position = 0;
            for (int i=0; i<shortest_distances.length; i++) {
                if(shortest_distances[i] > shortest_distances[position]) {
                    position = i;
                }
            }

            if (distance(word, nextWord) < shortest_distances[position]) {
                shortest_distances[position] = distance(word, nextWord);
                best_words[position] = nextWord;
            }
        }
        return best_words;
    }

    //Methode donnee pour mesurer la proximité
    //de deux mots
    public int distance(String s1, String s2) {

        int edits[][]=new int[s1.length()+1][s2.length()+1];
        s1 = s1.toLowerCase();

        for(int i=0;i<=s1.length();i++) {
            edits[i][0] = i;
        }
        for(int j=1;j<=s2.length();j++) {
            edits[0][j] = j;
        }

        for(int i=1;i<=s1.length();i++){
            for(int j=1;j<=s2.length();j++){
                int u=(s1.charAt(i-1)==s2.charAt(j-1)?0:1);
                edits[i][j]=Math.min(  edits[i-1][j]+1,
                        Math.min(  edits[i][j-1]+1,
                                edits[i-1][j-1]+u  )
                );
            }
        }
        return edits[s1.length()][s2.length()];
    }


    //Methode qui met a jour la liste des 5 mots les plus proches
    //quand on clique sur un mot.
    public void updateList(  String word, Model m,  DefaultListModel<String> list  ){

        String[] options = similarWords(  word, m  );
        list.removeAllElements();

        for (  int i = 0; i < options.length; i++  ){
            list.addElement(  options[i]  );
        }

    }

    //une des methodes principales du programme. Elle teste
    //chaque mot dans la JTextArea et verifie si elle existe
    //dans le dictionnaire. Elle le surligne sinon. Aussi,
    //Les mots surlignés sont mis dans une ArrayList. On
    //utlise cette ArrayList après pour ne faire apparaitre
    //une liste de 5 mots que pour ces mots-ci.
    public void checkWords(  JTextArea text, Model dict  ) throws BadLocationException {

        removeHighlights(  text  );
        hilited.clear();

        String[] arr = createTokens(  text.getText()  );
        String s = text.getText();
        Highlighter highlighter = text.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);


        for (  int i = 0; i < arr.length; i++  ){

            if(  !dict.dictionnaire.contains(  arr[i]  )  ){

                int index = 0;
                while(  true  ) {
                    int p0 = s.toLowerCase().indexOf(arr[i], index);
                    if(  p0 == -1  ){
                        break;
                    }
                    else {

                        int p1 = p0 + arr[i].length();
                        highlighter.addHighlight(p0, p1, painter);
                        index = p1;

                        if(  !hilited.contains(  text.getText(  p0, p1-p0  )  )  ) {
                                hilited.add(text.getText(p0, p1 - p0));
                        }

                    }

                }

            }

        }

    }


    //methode utile qui supprime tout surlignage du JTextArea
    public void removeHighlights(JTextArea textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i = 0; i < hilites.length; i++) {
            hilite.removeHighlight(hilites[i]);
        }

    }

}
