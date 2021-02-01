package data;

import armazem.Produto;

import java.sql.*;
import java.util.*;


public class ProdutoDAO implements Map<String, Produto> {
    private static ProdutoDAO singleton = null;

    private ProdutoDAO() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
             String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                          "codProduto varchar(7) NOT NULL PRIMARY KEY," +
                          "nomeProduto varchar(45) NOT NULL)";
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
    public static ProdutoDAO getInstance() {
        if (ProdutoDAO.singleton == null) {
            ProdutoDAO.singleton = new ProdutoDAO();
        }
        return ProdutoDAO.singleton;
    }

    /**
     * @return número de produtos na base de dados
     */
    @Override
    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM produtos")) {
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
     * Método que verifica se existem produtos
     *
     * @return true se existirem 0 produtos
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Método que verifica se um codigo de produto existe na base de dados
     *
     * @param code codigo do produto
     * @return true se o produto existe
     * @throws NullPointerException
     */
    @Override
    public boolean containsKey(Object code) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs =
                     stm.executeQuery("SELECT codProduto FROM produtos WHERE codProduto='"+code.toString()+"'")) {
            r = rs.next();
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    /**
     * Verifica se um produto existe na base de dados
     *
     * @param value
     * @return true caso o produto exista, false caso contrario
     * @throws NullPointerException
     */
    @Override
    public boolean containsValue(Object value) {
        Produto t = (Produto) value;
        Produto p = this.get(t.getCodProd());
        if(p==null){
            return false;
        } else{
            return t.equals(p);          
        }
    }

    /**
     * Obter um produto, dado o seu codigo
     *
     * @param code codigo do produto
     * @return o produto caso exista (null noutro caso)
     * @throws NullPointerException 
     */
    @Override
    public Produto get(Object code) {
        Produto p = null;
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM produtos WHERE codProduto='"+code+"'")) {
            if (rs.next()) {
                // O codigo existe na tabela
                // Reconstruir a colecção de paletes do produto
                Collection<String> paletes = getPaletesProduto((String)code, stm);


                // Reconstruir o produto com os dados obtidos da BD
                try(ResultSet rsa = stm.executeQuery("SELECT * FROM produtos WHERE codProduto='"+code+"'")){
                    if (rsa.next()){
                        p = new Produto(rsa.getString("codProduto"), rsa.getString("nomeProduto"), paletes);
                    }
                }
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return p;
    }

    /**
     * Método auxiliar que obtem a lista de paletes do com determinado código de Produto
     *
     * @param codProduto o codigo do produto
     * @return a lista de paletes do produto
     */
    private Collection<String> getPaletesProduto(String codProduto, Statement stm) throws SQLException {
        Collection<String> paletes = new TreeSet<>();
        try (ResultSet rs = stm.executeQuery("SELECT codQR FROM paletes WHERE codProduto='"+codProduto+"'")) {
            while(rs.next()) {
                paletes.add(rs.getString("codQR"));
            }
        }
        return paletes;
    }


    @Override
    public Produto put(String code, Produto p) {
        Produto res = null;       

        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            // Actualizar o Produto
            stm.executeUpdate("INSERT INTO produtos VALUES ('"+p.getCodProd() + "','" + p.getNomeProd() +"')" +
                    "ON DUPLICATE KEY UPDATE codProduto=VALUES(codProduto)");

            // Actualizar as paletes do produto
            Collection<String> oldPalete = getPaletesProduto(code, stm);
            Collection<String> newPalete = p.getPaletes();
            newPalete.removeAll(oldPalete);    /** paletes que entram no produto, em relação ao que está na BD */
            oldPalete.removeAll(newPalete);    /** paletes que saem no produto, em relação ao que está na BD */
            try (PreparedStatement pstm = conn.prepareStatement("UPDATE paletes SET codProduto=? WHERE codProduto=?")) {

                pstm.setNull(1, Types.VARCHAR);
                for (String s: oldPalete) {
                    pstm.setString(2, s);
                    pstm.executeUpdate();
                }

                pstm.setString(1, p.getCodProd());
                for (String a: newPalete) {
                    pstm.setString(2, a);
                    pstm.executeUpdate();
                }
            }

        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }

    /**
     * Remover um Produto, dado o seu codigo
     *
     * @param code codigo do Produto a remover
     * @return o Produto removido
     * @throws NullPointerException 
     */
    @Override
    public Produto remove(Object code) {
        Produto p = this.get((String)code);
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement();
             PreparedStatement pstm = conn.prepareStatement("UPDATE paletes SET Produto=? WHERE codProduto=?")) {
            
            pstm.setNull(1, Types.VARCHAR);
            for (String a: p.getPaletes()) {
                pstm.setString(2, a);
                pstm.executeUpdate();
            }
            
            stm.executeUpdate("DELETE FROM produtos WHERE codProduto='"+code+"'");
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return p;
    }

    /**
     * Adicionar um conjunto de produtos à base de dados
     *
     * @param produtos os produtos a adicionar
     * @throws NullPointerException 
     */
    @Override
    public void putAll(Map<? extends String,? extends Produto> produtos) {
        for(Produto p : produtos.values()) {
            this.put(p.getCodProd(), p);
        }
    }

    /**
     * Apagar todos os produtos
     *
     * @throws NullPointerException 
     */
    @Override
    public void clear() {
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.execute("UPDATE paletes SET codProduto=NULL");
            stm.executeUpdate("TRUNCATE produtos");
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

   /**
     * 
     * @return Todos os codigos de produto da base de dados
     */
    @Override
    public Set<String> keySet() {
        Set<String> codProduto = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery("SELECT codProduto FROM produtos ")){
            while (rs.next()) {   
                codProduto.add(rs.getString("codProduto"));
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return codProduto;
    }
    /**
     * @return Todos os produtos da base de dados
     */
    @Override
    public Collection<Produto> values() {
        Collection<Produto> res = new HashSet<>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codProduto FROM produtos")) { // ResultSet com os codigos de todos os produtos
                while (rs.next()) {
                    res.add(this.get(rs.getString("codProduto")));
            }
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return res;
    }


    @Override
    public Set<Entry<String, Produto>> entrySet() {
        Set<Entry<String, Produto>> setReturn = new HashSet<Map.Entry<String, Produto>>();
        try (Connection conn = DriverManager.getConnection(DAOconfig.URL, DAOconfig.USERNAME, DAOconfig.PASSWORD);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT codProduto FROM produtos")) {
                while(rs.next()){
                    Map.Entry<String, Produto> entry = new HashMap.SimpleEntry<String, Produto>(rs.getString("codProduto"), this.get(rs.getString("codProduto")));
                    setReturn.add(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new NullPointerException(e.getMessage());
            }
        return setReturn;
    }
}