
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.*;

//Mohamed Elayat et Fatima Mostefai


//Classe View qui contient tous les éléments graphiques
//ainsi que quelques methodes pour ouvrir/enregistrer des
//fichiers etc...
public class View  {

    public JMenuBar menuBar = new JMenuBar();
    public JMenuItem ouvrir = new JMenuItem(  "ouvrir"  );
    public JMenuItem enregistrer = new JMenuItem(  "enregistrer");
    public JMenu fichier = new JMenu(  "Fichier"  );
    public JMenuItem dictionnaire = new JMenuItem(  "Dictionnaire"  );
    public JMenuItem verifier = new JMenuItem (  "Vérifier"  );

    public JTextArea text = new JTextArea(  "Veuillez sélectionner un dictionnaire avant de vérifier."  );
    public JPanel[] panelHolder = new JPanel[2];
    public JFrame frame = new JFrame(  "Correcteur"  );
    public JScrollPane scroll = new JScrollPane(  text,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS  );
    public JList list = new JList();
    public DefaultListModel<String> lModel = new DefaultListModel<>();

    public GridLayout layout = new GridLayout(  2,1  );

    public int start;
    public int end;
    public String word;

    public Model instance;


    //Constructeur qui lance l'interface graphique et
    //qui lie tous les éléments ensembles.
    public View(  Model m  ){

        instance = m;

        list.setBackground(  Color.pink  );
        list.setModel(  lModel  );

        panelHolder[0] = new JPanel();
        panelHolder[0].setLayout(  new BorderLayout()  );
        panelHolder[0].add(  scroll  );

        panelHolder[1] = new JPanel();
        panelHolder[1].setBackground(  Color.pink  );
        panelHolder[1].setLayout(  new BorderLayout()  );
        panelHolder[1].add(  list  );

        frame.setSize(  600, 600  );
        frame.setDefaultCloseOperation(  JFrame.EXIT_ON_CLOSE  );

        text.setLineWrap(true);
        text.setWrapStyleWord(  true  );

        fichier.add(  ouvrir  );
        fichier.add(  enregistrer  );
        menuBar.add(  fichier  );
        menuBar.add(  dictionnaire  );
        menuBar.add(  verifier  );

        frame.setLayout(  layout  );
        frame.add(  panelHolder[0]  );
        frame.add(  panelHolder[1]  );
        frame.setJMenuBar(  menuBar  );
        frame.setVisible(  true  );
    }

    //Methode qui retourne l'addresse du fichier qu'on a
    //choisi d'ouvrir avec l'interface graphique
    public static String getFileFromGUI(){

        JFileChooser opener = new JFileChooser(  FileSystemView.getFileSystemView().getHomeDirectory()  );
        int returnValue = opener.showOpenDialog(  null  );

        if(  returnValue == JFileChooser.APPROVE_OPTION  ){
            String path = opener.getSelectedFile().getAbsolutePath();
            return path;
        }
        else {  return "";  }

    }

    //Methode qui prend le contenu d'un fichier texte
    //choisi et le met dans le JTextArea
    public void openIntoTextArea () throws IOException{

        text.setText("");
        BufferedReader reader = new BufferedReader(  new FileReader(  getFileFromGUI() ) );
        String line = reader.readLine();
        instance.removeHighlights(  text  );
        while (  line != null  ){
            text.append(  line  + " ");
            line = reader.readLine();
        }

    }

    //Methode qui permet d'enregistrer le contenu de la JTextArea
    public void saveAs () throws IOException{

        JFileChooser save = new JFileChooser();
        save.setApproveButtonText(  "Enregistrer"  );
        int actionDialog = save.showOpenDialog(null);
        if(actionDialog != JFileChooser.APPROVE_OPTION){
            return;
        }

        File fileName = new File(  save.getSelectedFile() + ".txt"  );
        BufferedWriter outFile = new BufferedWriter(  new FileWriter(  fileName  )  );
        text.write(  outFile  );

    }

    //3 Methodes qui lient la classe View et la class Controller
    //Elles specifient quelle sera la reaction du programme
    //suite a ces 3 types d'évenements. Les methodes sont
    //implémentées dans Controller.java
    public void addActionListeners(  ActionListener a  ) {
        enregistrer.addActionListener(  a  );
        ouvrir.addActionListener(  a  );
        dictionnaire.addActionListener(  a  );
        verifier.addActionListener(  a  );
    }

    public void addListSelectionListeners(  ListSelectionListener  l  ){
        list.getSelectionModel().addListSelectionListener(    l    );
    }

    public void addMouseListeners(  MouseListener m  ){
        text.addMouseListener(    m    );
    }

}
