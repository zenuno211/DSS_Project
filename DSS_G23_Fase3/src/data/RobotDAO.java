package data;

import armazem.Palete;
import armazem.Produto;
import armazem.Robot;
import utils.conversorStringList;
import java.util.List;

import java.sql.*;
import java.util.*;

/**
 * DAO para Robots
 *
 *
 */
public class RobotDAO implements Map<String, Robot> {
    private static RobotDAO singleton = null;

    private RobotDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                    "codProduto varchar(7) NOT NULL PRIMARY KEY," +
                    "nomeProduto varchar(45) NOT NULL)";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS paletes (" +
                    "codQR VARCHAR(7) NOT NULL PRIMARY KEY," +
                    "codProduto VARCHAR(7) NOT NULL,"+
                    "tamanho DECIMAL(5,2) NOT NULL," +
                    "localizacao VARCHAR(5) NOT NULL)";
            stm.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS robots (" +
                  "codRobot VARCHAR(7) NOT NULL PRIMARY KEY," +
                  "codPalete VARCHAR(7) DEFAULT NULL," +
                  "percurso VARCHAR(60) DEFAULT NULL," +
                  "localizacao VARCHAR(7) NOT NULL," +
                  "ocupado Boolean NOT NULL," +
                  "recolhaFeita Boolean NOT NULL)"; 
            stm.executeUpdate(sql);
           } catch (SQLException e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * Implementação do padrão Singleton
     *
     * @return devolve a instância única desta classe
     */
    public static RobotDAO getInstance() {
        if (RobotDAO.singleton == null) {
            RobotDAO.singleton = new RobotDAO();
        }
        return RobotDAO.singleton;
    }

    /**
     * @return número de robots na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM robots")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            // Erro a criar tabela...
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    /**
     * Método que verifica se existem robots
     *
     * @return true se existirem 0 robots
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Método que verifica se um codigo de robot existe na base de dados
     *
     * @param code codigo do robot
     * @return true se o robot existe
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object code) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT codRobot FROM robots WHERE codRobot='"+code.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se um robot existe na base de dados
     *
     * @param value
     * @return true caso o robot exista, false caso contrario
     * @throws NullPointerException 
     */
    @Override
    public boolean containsValue(Object value) {
        Robot r = (Robot) value;
        Robot robot = this.get(r.getCodRobot());
        if(robot==null){
            return false;
        } else{
            return r.equals(robot);
        }
    }

    /**
     * Obter um robot, dado o seu codigo
     *
     * @param code codigo do robot
     * @return o robot caso exista (null noutro caso)
     * @throws NullPointerException
     */
    @Override
    public Robot get(Object code) {
        Robot r = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM robots WHERE codRobot='"+code+"'")) {
                if (rs.next()) {
                r = new Robot(rs.getString("codRobot"), rs.getString("codPalete"), conversorStringList.stringToList(rs.getString("percurso")), rs.getString("localizacao"), rs.getBoolean("ocupado"), rs.getBoolean("recolhaFeita")/* , rs.getString("locPalete"), rs.getString("locDestino") */);
                     
              
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Método auxiliar que obtem a lista de paletes do com determinado código de Produto
     *
     * @param codProduto o codigo do produto
     * @return a lista de paletes do produto
     */
    private Collection<String> getPaletesProduto(String codProduto, Statement stm) throws SQLException {
        Collection<String> paletes = new TreeSet<>();
        try (ResultSet rsa = stm.executeQuery("SELECT codQR FROM paletes WHERE codProduto='"+codProduto+"'")) {
            while(rsa.next()) {
                paletes.add(rsa.getString("codQR"));
            }
        } 
        return paletes;
    }


    @Override
    public Robot put(String code, Robot r) {
        Robot res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            String stringPercurso = "";
            if(r.getPercurso() == null){
                stringPercurso = null;
            } else {
                stringPercurso = conversorStringList.listToString(r.getPercurso().getPercurso());
            }
            int ocupado = 0;
            if(r.getOcupado()){
                ocupado = 1;
            }

            int recolha = 0;
            if(r.getRecolhaFeita()){
                recolha = 1;
            }

            stm.executeUpdate("INSERT INTO robots VALUES ('"+r.getCodRobot() + "','" +r.getCodPalete() + "','" + stringPercurso + "','" + 
                                                             r.getLocalizacao() + "','"  + ocupado + "','" + 
                                                             recolha  +  "' ) "  + "ON DUPLICATE KEY UPDATE codPalete=VALUES(codPalete)," + "percurso=VALUES(percurso)," +
                                                             "localizacao=VALUES(localizacao)," + "ocupado=VALUES(ocupado)," + 
                                                             "recolhaFeita=VALUES(recolhaFeita)");


        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    @Override
    public Robot remove(Object code) {
        Robot t = this.get(code);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM robots WHERE codRobot='"+code+"'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    /**
     * Adicionar um conjunto de robots à base de dados
     *
     * @param robots a adicionar
     * @throws NullPointerException
     */
     @Override
     public void putAll(Map<? extends String, ? extends Robot> robots) {
        for(Robot r : robots.values()) {
            this.put(r.getCodRobot(), r);
        }
    }

    /**
     * Apagar todos os robots
     *
     * @throws NullPointerException
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE robots");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * 
     * @return todos os codigos dos robots na BD 
     */
    @Override
    public Set<String> keySet() {
        Set<String> robots = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT codRobot FROM robots")){
            while (rs.next()) {  
                robots.add(rs.getString("codRobot"));
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return robots;
    }

    /**
     * @return Todos os robots da base de dados
     */
    @Override
    public Collection<Robot> values() {
        Collection<Robot> col = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT codRobot FROM robots")) {
            while (rs.next()) {   // Utilizamos o get para construir os robots
                col.add(this.get(rs.getString("codRobot")));
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }


    @Override
    public Set<Entry<String, Robot>> entrySet() {
        Set<Entry<String, Robot>> setReturn = new HashSet<Map.Entry<String, Robot>>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codRobot FROM robots")) {
                while(rs.next()){
                    Map.Entry<String, Robot> entry = new HashMap.SimpleEntry<String, Robot>(rs.getString("codRobot"), this.get(rs.getString("codRobot")));
                    setReturn.add(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        return setReturn;
    }
}

