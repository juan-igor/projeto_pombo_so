/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto_pombo_so;

import javax.swing.JLabel;

/**
 *
 * @author Juan Igor
 */
public class C_Msg{
    
    private int qtd_msg, qtd_msg_max;
    private JLabel label_qtd_msg;
    
    public C_Msg(JLabel label_qtd_msg, int qtd_max){
        this.label_qtd_msg = label_qtd_msg;
        this.qtd_msg_max = qtd_max;
    }
    
    public void retira_mensagens(int qtd){
        qtd_msg -= qtd;
        label_qtd_msg.setText(""+qtd_msg);
    }
    
    public void insere_mensagem(int qtd){
        if(qtd_msg <= qtd_msg_max){
            qtd_msg += qtd;
            label_qtd_msg.setText(""+qtd_msg);
        }
    }
    
    public int get_qtd_msg(){
        return qtd_msg;
    }
}
