
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

//Mohamed Elayat et Fatima Mostefai


//Classe Controller qui lie les methodes de la classe Model
//et les �lements graphique de la classe View.
public class Controller implements MouseListener, ActionListener, ListSelectionListener {

    public View m_view;
    public Model m_model;

    public Controller(  View v, Model m ){

        m_view = v;
        m_model = m;

        v.addActionListeners(  this  );
        v.addListSelectionListeners(  this  );
        v.addMouseListeners(  this  );

    }


    //Implantation de la methode actionPerformed.
    //Dependamment du bouton cliqu�, le programm va soit
    //lire un fichier externe, enregistrer un fichier externe,
    //cr�er une structure de donn�es d'un dictionnaire ou
    //v�rifier l'orthographe du JTextArea
    public void actionPerformed(  ActionEvent optionChosen  ) {

        try {

            if (optionChosen.getSource() == m_view.enregistrer) {
                m_view.saveAs();
            } else if (optionChosen.getSource() == m_view.ouvrir) {
                m_view.openIntoTextArea();
            } else if (optionChosen.getSource() == m_view.dictionnaire) {

                String temp = m_view.getFileFromGUI();
                if(  temp != ""  ) {
                    m_view.instance = new Model(  temp  );
                }

            } else if (optionChosen.getSource() == m_view.verifier) {

                if(  !m_view.text.getText().equals(  ""  )  ){

                    try {
                        m_model.checkWords(m_view.text, m_view.instance);
                    }

                    catch(  NullPointerException e  ){
                        System.out.println("Avant de v�rifier, il faut sp�cifier un dictionnaire." +
                                " Veuillez choisir un fichier valide et r�-essayez.");
                    }
                }
            }
        }

        catch(  BadLocationException e ){
            System.out.println(  "Bad Location exception"  );
            System.out.println(  e.offsetRequested()  );
            System.out.println(  e.getStackTrace()  );
        }

        catch(  IOException e  ){
            System.out.println(  "IOEexception"  );
            System.out.println(  e.getStackTrace()  );
        }
    }


    //Implantation de la m�thode mouseClicked. Cette
    //m�thode affiche la liste des 5 mots les plus proches
    //si le mot est surlign� en rouge. Sinon, elle n'affiche
    //rien.

    public void mouseClicked(  MouseEvent e  ){

        try {

            int offset = m_view.text.viewToModel(e.getPoint());
            m_view.start = Utilities.getWordStart(  m_view.text, offset  );
            m_view.end = Utilities.getWordEnd(  m_view.text, offset  );

            m_view.word = m_view.text.getDocument().getText(  m_view.start, m_view.end - m_view.start  );

            Highlighter hilite = m_view.text.getHighlighter();
            Highlighter.Highlight[] hilites = hilite.getHighlights();

            m_view.lModel.removeAllElements();

            for(  int i = 0; i < hilites.length; i++  ){

                if(  offset >= hilites[i].getStartOffset() && offset <= hilites[i].getEndOffset()  ){
                    m_model.updateList(m_view.word, m_view.instance, m_view.lModel);
                    break;
                }

            }

            m_view.frame.setVisible(  true  );

        }

        catch(  BadLocationException b  ){
            System.out.println(  "Bad location exception"  );
            b.printStackTrace();
        }

    }


    public void mouseEntered(  MouseEvent e  ){}
    public void mouseExited(  MouseEvent e  ){}
    public void mousePressed(  MouseEvent e  ){}
    public void mouseReleased(  MouseEvent e  ){}

    //Implantation de la m�thode valueChanged.
    //Cette m�thode remplace le mot surlign� par
    //le mot choisi dans la liste. Elle enleve
    //le surlignage ensuite.
    public void valueChanged(  ListSelectionEvent e  ){

        if (m_view.list.getSelectedValue() != null) {

            if (!e.getValueIsAdjusting()) {

                String newWord = m_view.list.getSelectedValue().toString();

                m_view.text.replaceRange(newWord, m_view.start, m_view.end);
                m_view.end = m_view.start + newWord.length();

                Highlighter hilite = m_view.text.getHighlighter();

                Highlighter.Highlight[] hilites = hilite.getHighlights();

                for (  int i = 0; i < hilites.length; i++  ){

                    if(  hilites[i].getEndOffset() == m_view.end  ){

                        hilite.removeHighlight(  hilites[i]  );

                    }

                }

            }

        }

    }

}
