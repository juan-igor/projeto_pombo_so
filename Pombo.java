/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projeto_pombo_so;

import java.awt.Point;
import java.util.concurrent.Semaphore;
import javax.swing.JLabel;

/**
 *
 * @author Juan Igor
 */
public class Pombo extends Thread{
    
    private boolean alive = true;
//    private Semaphore mutex, full, empty;
    private int carga, img_pos_x, img_pos_y;
    private float dist_c_msg, tempo_carga, tempo_descarga, tempo_voo;
    private int qtd_viagens = 0;
    private long init_time = 0;
    private C_Msg c_msg_destino, c_msg;
    private JLabel img_pombo, status_pombo;
    
    public Pombo(JLabel status_pombo, C_Msg c_msg, C_Msg c_msg_destino, float dist, int N, float TC, float TD, float TV){
        this.carga = N;
//        this.mutex = mutex;
//        this.full = full;
//        this.empty = empty;
        this.dist_c_msg = dist;
        this.c_msg = c_msg;
        this.c_msg_destino = c_msg_destino;
        this.img_pombo = JFrame_Execucao.label_pombo;
        this.status_pombo = status_pombo;
        this.tempo_carga = TC * (float)1000;
        this.tempo_descarga = TD * (float)1000;
        this.tempo_voo = TV * (float)1000;
        this.img_pos_x = img_pombo.getX();
        this.img_pos_y = img_pombo.getY();
        this.setPriority(3);
    }
    
    @Override
    public void run(){
        while(alive){
            try {
                if(JFrame_Execucao.FULL.availablePermits() < carga){
                    img_pombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/pombo_sleep.png")));
                    status_pombo.setText("Repousando...");
                }
//                for(int i=0; i<carga; i++){
//                    JFrame_Execucao.FULL.acquire();
//                }
                JFrame_Execucao.FULL.acquire(carga);
            } catch (InterruptedException ex) {}
            JFrame_Execucao.EMPTY.release(carga);
            try { JFrame_Execucao.MUTEX.acquire(); } catch (InterruptedException ex){}
            carrega_msg();
            JFrame_Execucao.MUTEX.release();
            init_time = System.currentTimeMillis();
            while((System.currentTimeMillis() - init_time) < tempo_carga){}
            
            voa_a_b();
            
            descarrega_msg();
            
            voa_b_a();
        }
        status_pombo.setText("Pombo libertado...");
        img_pombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/fundo72x72.png")));
    }
    
    public void carrega_msg(){
        img_pombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/pombo_load.png")));
        status_pombo.setText("Carregando Mensagens...");
        c_msg.retira_mensagens(carga);        
    }
    
    public void descarrega_msg(){
        img_pombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/pombo_unload.png")));
        int pos = img_pos_x + 391;
        img_pombo.setBounds(pos, img_pos_y, 72, 72);
        status_pombo.setText("Descarregando Mensagens...");
        c_msg_destino.insere_mensagem(carga);
        qtd_viagens++;
        
        init_time = System.currentTimeMillis();
        while((System.currentTimeMillis() - init_time) < tempo_descarga){
            img_pombo.setBounds(pos, img_pos_y, 72, 72);
        }
    }
    
    public void voa_a_b(){
        img_pombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/pombo_flying_toRight.png")));
        status_pombo.setText("Voando...");
        long delay = (long) ((float)(tempo_voo / dist_c_msg) * (float)(10));
        int pos = img_pos_x;
        for(int i=0; i < dist_c_msg; i++){
            img_pombo.setBounds(++pos, img_pos_y, 72, 72);
            long time_tmp = System.currentTimeMillis() * 10;
            while(((System.currentTimeMillis()*10) - time_tmp) < delay){}
        }
    }
    
    public void voa_b_a(){
        img_pombo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/projeto_pombo_so/fotos/pombo_flying_toLeft.png")));
        status_pombo.setText("Voando...");
        long delay = (long) ((float)(tempo_voo / dist_c_msg) * (float)(10));
        int pos = img_pos_x + 391;
        for(int i=0; i < dist_c_msg; i++){
            img_pombo.setBounds(--pos, img_pos_y, 72, 72);
            long time_tmp = System.currentTimeMillis()*10;
            while(((System.currentTimeMillis()*10) - time_tmp) < delay){}
        }
    }
    
    public void libertar_pombo(){
        this.alive = false;
    }
    
    public int get_qtd_viagens(){
        return qtd_viagens;
    }
    
    public int get_qtd_max_msg(){
        return carga;
    }
    
    public float get_tv(){
        return tempo_voo;
    }
    
    public float get_tc(){
        return tempo_carga;
    }
    
    public float get_td(){
        return tempo_descarga;
    }
}
