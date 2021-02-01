package ui;

import armazem.*;

import java.util.List;
import java.util.Scanner;
import Exceptions.*;

/**
 * Exemplo de interface em modo texto.
 *
 */
public class TextUI {

    private SSArmazem model;

    private Scanner scin;

    public TextUI() {
        
        this.model = new GestorArmazem();
        scin = new Scanner(System.in);
    }


    
    

    /**
     * Executa o menu principal e invoca o método correspondente à opção seleccionada.
     */
    public void run() {
        
        this.menuPrincipal();
        System.out.println("Até breve!...");
    }


    private void menuPrincipal(){
        Menu menu = new Menu(new String[]{
            "Menu de Gestor.",
            "Menu de Robot.",
            "Menu de Leitor."
        });
        

        menu.setHandler(1, ()->menuGestor());
        menu.setHandler(2, ()->menuRobot());
        menu.setHandler(3, ()->menuLeitor());

        menu.executa(1);
    }

    private void menuGestor(){
        Menu menu = new Menu(new String[]{
            "Listar localizações.",
            "Notificar para transporte."
        });
        
        
        menu.setHandler(1, ()->trataListarLocalizacao());
        menu.setHandler(2, ()->tratarNotificacaoParaTransporte());

        menu.executa(2);
    }

    private void menuRobot(){
        Menu menu = new Menu(new String[]{
            "Notificar recolha.",
            "Notificar entrega."
        });
        
        menu.setHandler(1, ()->trataNotificarRecolha());
        menu.setHandler(2, ()->trataNotificarEntrega());

        menu.executa(3);
    }
    private void menuLeitor(){
        Menu menu = new Menu(new String[]{
                "Ler código QR e registar Palete."
        });

        menu.setHandler(1, ()->trataRegistarPalete());

        menu.executa(4);
    }





    private void trataListarLocalizacao(){
        try{
            String leftAlignFormat = "| %-15s | %-25s | %-15s | %-15s |%n";
            System.out.format("-----------------------------------------------------------------------------------%n");
            System.out.format("|      Palete     |          Produto          |      Estado     |   Localização   |%n");
            System.out.format("+-----------------+---------------------------+-----------------+-----------------+%n");
            for (Palete p : this.model.getListPaletes()) {

                String estado;
                String loc = p.getLocalizacao();
                if(loc.charAt(0) == 'R') 
                    estado = "Em Transporte";
                else if (loc.charAt(0) == 'P') //prateleira
                    estado = "Armazenada";
                else if (loc.startsWith("ZE"))
                    estado = "Entregue";
                else
                    estado = "Em espera";

                System.out.format(leftAlignFormat, p.getCodQR() , p.getCodProduto()+ " -> " +this.model.getProduto(p.getCodProduto()).getNomeProd(), estado, p.getLocalizacao());
            }
            System.out.format("-----------------------------------------------------------------------------------%n");
        }
        catch(NullPointerException e){
            System.out.println(e.getMessage());
        }
    }


    private void tratarNotificacaoParaTransporte() {
        try{
            System.out.print("Palete para transportar: ");
            String codPalete = scin.nextLine();
            if (!this.model.existePalete(codPalete)){
                throw new NoExistsPaleteException();
            }

            String locPalete = this.model.determinarLocalizacao(codPalete);


            String locFinal = null;
            if(locPalete.startsWith("ZR")){
                
            locFinal = this.model.getPrateleiraVazia();
                

            } else if (locPalete.startsWith("P")){
                locFinal = "ZE01";
                
            } else {
                throw new LocalizacaoInvalidaException();
            }

            List<String> robots = this.model.getListaRobotsDisponiveis();

            Robot robotMaisProximo = this.model.getRobotMaisProximo(locPalete, robots);

            Percurso percurso = model.calcularPercurso(robotMaisProximo.getCodRobot(), locPalete, locFinal);
        
            model.atualizarEstadoRobot(robotMaisProximo.getCodRobot(), percurso, codPalete);

            if(locFinal.startsWith("P")){
                model.atualizarEstadoPrateleira(locFinal, true);
            }            
            
        }catch(LocalizacaoInvalidaException | NullPointerException | NoRobotDisponivelException | NoPrateleirasVaziasException | NoExistsPaleteException e){
            if(e instanceof NoRobotDisponivelException){
                System.out.println("Operação cancelada. Nenhum robot disponivel para transporte.");
            } else if (e instanceof NoPrateleirasVaziasException) {
                System.out.println("Operação cancelada. Não existem prateleiras vazias.");
            } else if(e instanceof NoExistsPaleteException){
                System.out.println("Operação cancelada. Palete indicada não existe");
            } else if(e instanceof LocalizacaoInvalidaException){
                System.out.println("Operação cancelada. Palete já se encontra na zona de entregas ou está em transporte.");
            } else {  
                System.out.println(e.getLocalizedMessage());
            }
        }

    }
        

    private void trataNotificarRecolha(){
        try{
            System.out.print("Robot para ação: ");
            String robot = scin.nextLine();

            if(!this.model.existeRobot(robot)){
                throw new NoExistsRobotException();
            }

            if(this.model.robotOcupadoSemPalete(robot)){
                this.model.registarRecolha(robot);
            } else {
                throw new NoRobotDisponivelException();
            }

            
        }catch (NullPointerException | NoExistsRobotException | NoRobotDisponivelException e){
            if( e instanceof NoExistsRobotException ){
                System.out.println("O robot escolhido nao existe.");
            }
            else if(e instanceof NoRobotDisponivelException){
                System.out.println("Robot não está disponível para efetuar recolha.");
            }
            else{
                System.out.println(e.getMessage());
            }
        
        }
    }

    private void trataNotificarEntrega(){
        try{
            System.out.print("Robot para ação: ");
            String robot = scin.nextLine();
            
            if(!this.model.existeRobot(robot)){
                throw new NoExistsRobotException();
            }

            if(this.model.robotOcupadoComPalete(robot)){
                this.model.registarEntrega(robot);
            } else {
                throw new NoRobotDisponivelException();
            }

        }catch(NullPointerException | NoExistsRobotException | NoRobotDisponivelException e){
            if( e instanceof NoExistsRobotException ){
                System.out.println("O robot escolhido nao existe.");
            }
            else if(e instanceof NoRobotDisponivelException){
                System.out.println("Robot não está disponível para efetuar entrega.");
            }
            else{
                System.out.println(e.getMessage());
            }
        }

    }

    private void trataRegistarPalete(){
        try{
            String codQR;
            System.out.println("Código QR:   ex:(P_xxxxx)");
            codQR = scin.nextLine();
                            
            if (this.model.existePalete(codQR)){
                throw new PaleteDuplicadaException();
            }

            System.out.println("Insira o código do produto (7 caratéres no máximo): ");
            String codProd = scin.nextLine();
            String nomeProd = null;

            if (!this.model.existeProd(codProd)) {
                System.out.println("Produto ainda não registado no sistema.");
                System.out.println("Insira o nome do produto: ");
                nomeProd = scin.nextLine();
            } else {
                System.out.println("Produto encontrado no sistema.");
            }

            this.model.registarPaleteSistema(codQR, codProd, nomeProd);
            System.out.println("Palete com código QR '" + codQR + "' e com código de produto '" + codProd + "' registada no sistema.");

        } catch(NullPointerException | PaleteDuplicadaException e){
            if (e instanceof PaleteDuplicadaException) {
                System.out.println(("Palete já existe no sistema. Insira um código diferente."));
            }
            else{
                System.out.println(e.getMessage());   
            }
        }
    }           
}