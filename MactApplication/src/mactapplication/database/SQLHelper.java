/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mactapplication.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mactapplication.model.CaseDetail;

/**
 *
 * @author Sabapathi
 */
public class SQLHelper {

    private final Connection conn;

    public SQLHelper() {
        conn = SQLConnection.connection();
    }

    public void createTables() {
        String mactTable = "CREATE TABLE IF NOT EXISTS MACT (ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , DATE_OF_ACC DATETIME, TIME DATETIME, PLACE TEXT, DR_NAME TEXT, CR_NAME TEXT, VEH_NO TEXT, GH TEXT, POLICE_ST TEXT, PET_NAME TEXT, MACT TEXT, MCOP TEXT, FIRST_HEAR TEXT, EP_NO TEXT, FIR TEXT, DATE_OF_WARRENT TEXT, NATURE BOOL, PUNISHMENT TEXT, DAR TEXT, FIP TEXT, BRANCH TEXT, TYPE_OF_ROAD TEXT, ROUTE TEXT)";
        try {

            PreparedStatement mactTableStr = conn.prepareStatement(mactTable);
            mactTableStr.execute();

        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public boolean insertCategory(CaseDetail caseDetail) {
        String query = "INSERT INTO MACT (DATE_OF_ACC, TIME, PLACE, DR_NAME, CR_NAME, VEH_NO, GH, POLICE_ST, PET_NAME, MACT, MCOP, FIRST_HEAR, EP_NO, FIR, DATE_OF_WARRENT, NATURE, PUNISHMENT, DAR, FIP, BRANCH, TYPE_OF_ROAD, ROUTE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            boolean isSuccess;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, caseDetail.getDateOfAcc());
            stmt.setString(2, caseDetail.getTime());
            stmt.setString(3, caseDetail.getPlace());
            stmt.setString(4, caseDetail.getDrName());
            stmt.setString(5, caseDetail.getCrName());
            stmt.setString(6, caseDetail.getVehNo());
            stmt.setString(7, caseDetail.getGh());
            stmt.setString(8, caseDetail.getPoliceStation());
            stmt.setString(9, caseDetail.getPetName());
            stmt.setString(10, caseDetail.getMact());
            stmt.setString(11, caseDetail.getMcop());
            stmt.setString(12, caseDetail.getFirstHear());
            stmt.setString(13, caseDetail.getEpNo());
            stmt.setString(14, caseDetail.getFir());
            stmt.setString(15, caseDetail.getDateOfWarrent());
            stmt.setString(16, caseDetail.getNature());
            stmt.setString(17, caseDetail.getPunishment());
            stmt.setString(18, caseDetail.getDar());
            stmt.setString(19, caseDetail.getFip());
            stmt.setString(20, caseDetail.getBranch());
            stmt.setString(21, caseDetail.getTypeOfRoad());
            stmt.setString(22, caseDetail.getRoute());

            int count = stmt.executeUpdate();
            isSuccess = count == 1;
            stmt.close();

            return isSuccess;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public ObservableList<CaseDetail> getAllCaseDetails() {
        ObservableList<CaseDetail> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM MACT";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.add(new CaseDetail(rs.getInt("ID"), rs.getString("DATE_OF_ACC"), rs.getString("TIME"),
                        rs.getString("PLACE"), rs.getString("DR_NAME"), rs.getString("CR_NAME"), rs.getString("VEH_NO"),
                        rs.getString("GH"), rs.getString("POLICE_ST"), rs.getString("PET_NAME"), rs.getString("MACT"),
                        rs.getString("MCOP"), rs.getString("FIRST_HEAR"), rs.getString("EP_NO"), rs.getString("FIR"),
                        rs.getString("DATE_OF_WARRENT"), rs.getString("NATURE"), rs.getString("PUNISHMENT"),
                        rs.getString("DAR"), rs.getString("FIP"), rs.getString("BRANCH"), rs.getString("TYPE_OF_ROAD"),
                        rs.getString("ROUTE")));
            }
            stmt.close();
            return data;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public CaseDetail getCaseDetailByFipNo(String fipNo) {
        String query = "SELECT * FROM MACT WHERE FIP = ?";
        CaseDetail caseDetail = null;
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fipNo);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                caseDetail = new CaseDetail(rs.getInt("ID"), rs.getString("DATE_OF_ACC"), rs.getString("TIME"),
                        rs.getString("PLACE"), rs.getString("DR_NAME"), rs.getString("CR_NAME"), rs.getString("VEH_NO"),
                        rs.getString("GH"), rs.getString("POLICE_ST"), rs.getString("PET_NAME"), rs.getString("MACT"),
                        rs.getString("MCOP"), rs.getString("FIRST_HEAR"), rs.getString("EP_NO"), rs.getString("FIR"),
                        rs.getString("DATE_OF_WARRENT"), rs.getString("NATURE"), rs.getString("PUNISHMENT"),
                        rs.getString("DAR"), rs.getString("FIP"), rs.getString("BRANCH"), rs.getString("TYPE_OF_ROAD"),
                        rs.getString("ROUTE"));
            }
            stmt.close();
            return caseDetail;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public boolean updateCaseDetails(CaseDetail caseDetail) {
        String query = "UPDATE MACT SET DATE_OF_ACC = ?, TIME = ?, PLACE = ?, DR_NAME = ?, CR_NAME = ?, VEH_NO = ?, GH = ?, POLICE_ST = ?, PET_NAME = ?, MACT = ?, MCOP = ?, FIRST_HEAR = ?, EP_NO = ?, FIR = ?, DATE_OF_WARRENT = ?, NATURE = ?, PUNISHMENT = ?, DAR = ?, FIP = ?, BRANCH = ?, TYPE_OF_ROAD = ?, ROUTE = ? WHERE ID = ?";
        try {
            boolean isSuccess;
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, caseDetail.getDateOfAcc());
            stmt.setString(2, caseDetail.getTime());
            stmt.setString(3, caseDetail.getPlace());
            stmt.setString(4, caseDetail.getDrName());
            stmt.setString(5, caseDetail.getCrName());
            stmt.setString(6, caseDetail.getVehNo());
            stmt.setString(7, caseDetail.getGh());
            stmt.setString(8, caseDetail.getPoliceStation());
            stmt.setString(9, caseDetail.getPetName());
            stmt.setString(10, caseDetail.getMact());
            stmt.setString(11, caseDetail.getMcop());
            stmt.setString(12, caseDetail.getFirstHear());
            stmt.setString(13, caseDetail.getEpNo());
            stmt.setString(14, caseDetail.getFir());
            stmt.setString(15, caseDetail.getDateOfWarrent());
            stmt.setString(16, caseDetail.getNature());
            stmt.setString(17, caseDetail.getPunishment());
            stmt.setString(18, caseDetail.getDar());
            stmt.setString(19, caseDetail.getFip());
            stmt.setString(20, caseDetail.getBranch());
            stmt.setString(21, caseDetail.getTypeOfRoad());
            stmt.setString(22, caseDetail.getRoute());
            stmt.setInt(23, caseDetail.getId());

            int count = stmt.executeUpdate();
            isSuccess = count == 1;
            stmt.close();

            return isSuccess;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean deleteCaseDetail(CaseDetail caseDetail) {
        String query = "DELETE FROM MACT WHERE ID = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, caseDetail.getId());
            int count = stmt.executeUpdate();
            boolean isSuccess = count == 1;
            stmt.close();
            return isSuccess;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
}
