package data;

import armazem.Palete;
import armazem.Produto;
import java.sql.*;
import java.util.*;


public class PaleteDAO implements Map<String, Palete> {
    private static PaleteDAO singleton = null;

    
    private PaleteDAO() {
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
    public static PaleteDAO getInstance() {
        if (PaleteDAO.singleton == null) {
            PaleteDAO.singleton = new PaleteDAO();
        }
        return PaleteDAO.singleton;
    }

    /**
     * @return número de paletes na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM paletes")) {
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
     * Método que verifica se existem paletes
     *
     * @return true se existirem 0 paletes
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Método que verifica se um QRcode de uma palete existe na base de dados
     *
     * @param code QRcode
     * @return true se a palete existe
     * @throws NullPointerException 
     */
    @Override
    public boolean containsKey(Object code) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT codQR FROM paletes WHERE codQR='"+code.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se uma Palete existe na base de dados
     *
     * @param value 
     * @return true caso q palete exista, false caso contrario
     * @throws NullPointerException 
     */
    @Override
    public boolean containsValue(Object value) {
        Palete a = (Palete) value;
        Palete g = this.get(a.getCodQR());
        if (g == null){
            return false;
        } else {
            return a.equals(g);
        }
    }

    /**
     * Obter uma Palete, dado o seu QRcode
     *
     * @param code QRcode da palete
     * @return a palete caso exista (null noutro caso)
     * @throws NullPointerException
     */
    @Override
    public Palete get(Object code) {
        Palete a = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM paletes WHERE codQR='"+code+"'")) {
            if (rs.next()) {  // A chave existe na tabela
                a = new Palete(rs.getString("codQR"), rs.getString("codProduto"), rs.getFloat("tamanho"), rs.getString("localizacao"));
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return a;
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
    public Palete put(String code, Palete p) {
        Palete res = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Actualizar a Palete
            stm.executeUpdate(
                    "INSERT INTO paletes VALUES ('"+p.getCodQR()+"', '"+p.getCodProduto()+ "','" +p.getTamanho()+"', '"+p.getLocalizacao()+"' ) " +
                            "ON DUPLICATE KEY UPDATE localizacao=VALUES(localizacao)");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    /**
     * Remover uma palete, dado o seu QRcode
     *
     * @param code QRcode da palete a remover
     * @return palete removida
     * @throws NullPointerException
     */
    @Override
    public Palete remove(Object code) {
        Palete t = this.get(code);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM paletes WHERE codQR='"+code+"'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return t;
    }

    /**
     * Adicionar um conjunto de paletes à base de dados
     *
     * @param paletes a adicionar
     * @throws NullPointerException 
     */
     @Override
     public void putAll(Map<? extends String,? extends Palete> paletes) {
        for(Palete p : paletes.values()) {
            this.put(p.getCodQR(), p);
        }
    }

    /**
     * Apagar todas as paletes
     *
     * @throws NullPointerException
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("TRUNCATE paletes");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    /**
     * 
     * @return todos os codigos das paletes na BD 
     */
    @Override
    public Set<String> keySet() {
        Set<String> paletes = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT codQR FROM paletes")){
            while (rs.next()) {  
                paletes.add(rs.getString("codQR"));
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return paletes;
    }

    /**
     * @return Todas as paletes da base de dados
     */
    @Override
    public Collection<Palete> values() {
        Collection<Palete> col = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT codQR FROM paletes")) {
            while (rs.next()) {   // Utilizamos o get para construir as paletes
                col.add(this.get(rs.getString("codQR")));
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return col;
    }

    @Override
    public Set<Entry<String, Palete>> entrySet() {
        Set<Entry<String, Palete>> setReturn = new HashSet<Map.Entry<String, Palete>>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codQR FROM paletes")) {
                while(rs.next()){
                    Map.Entry<String, Palete> entry = new HashMap.SimpleEntry<String, Palete>(rs.getString("codQR"), this.get(rs.getString("codQR")));
                    setReturn.add(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        return setReturn;
    }

}
