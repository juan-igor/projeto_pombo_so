/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto_pombo_so;

import java.util.concurrent.Semaphore;
import javax.swing.JLabel;

/**
 *
 * @author Juan Igor
 */
public class Usuario extends Thread{
    
//    private Semaphore empty, full, mutex;
    private JLabel img_user, label_id;
    private C_Msg caixa_msg;
    private int ID;
    private int qtd_msg_escrita = 0;
    private float tempo_escrita;
    private long init_time;
    private boolean alive = true;
    
    public Usuario(C_Msg caixa_msg, JLabel img_user, JLabel label_id, int ID, float TE){
        this.caixa_msg = caixa_msg;
        this.img_user = img_user;
        this.label_id = label_id;
//        this.empty = EMPTY;
//        this.full = FULL;
//        this.mutex = MUTEX;
        this.ID = ID;
        this.tempo_escrita = TE * 1000;
        this.setPriority(2);
    }
    
    @Override
    public void run(){
        while(alive){
//            if(JFrame_Execucao.EMPTY.availablePermits() == 0){
//                img_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/user_sleep.png")));
//            }
//            escreve_msg();
            try{
                if(JFrame_Execucao.EMPTY.availablePermits() == 0){
                    img_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/user_sleep.png")));
                }
                JFrame_Execucao.EMPTY.acquire();
                if(!alive){
                  JFrame_Execucao.EMPTY.release();  
                }
            } catch(InterruptedException e){}
            if(alive){
                escreve_msg();
                insere_mensagem();
                JFrame_Execucao.FULL.release();
            }
        }
        img_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/fundo72x72.png")));
        label_id.setText("");
    }
    
    public void escreve_msg(){
        qtd_msg_escrita++;
        img_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/user_write.png")));
        init_time = System.currentTimeMillis();
        while((System.currentTimeMillis() - init_time) < Math.round(tempo_escrita)){}
    }
    
    public void insere_mensagem(){
        img_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/user_msg_add.png")));
        try{
            if(JFrame_Execucao.MUTEX.availablePermits() == 0){
                img_user.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/user_sleep.png")));
            }
            JFrame_Execucao.MUTEX.acquire();
        } catch(InterruptedException e){}
        caixa_msg.insere_mensagem(1);
        JFrame_Execucao.MUTEX.release();
    }
    
    public void excluir_usuario(){
        alive = false;
    }
    
    public int get_id(){
        return ID;
    }
    
    public float get_te(){
        return tempo_escrita;
    }
    
    public int get_qtd_escrita(){
        return qtd_msg_escrita;
    }
}
