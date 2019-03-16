package application.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CaseDetail extends RecursiveTreeObject<CaseDetail> {

	public SimpleIntegerProperty id;
	public SimpleStringProperty dateOfAcc;
	public SimpleStringProperty time;
	public SimpleStringProperty place;
	public SimpleStringProperty drName;
	public SimpleStringProperty crName;
	public SimpleStringProperty vehNo;
	public SimpleStringProperty gh;
	public SimpleStringProperty policeStation;
	public SimpleStringProperty petName;
	public SimpleStringProperty mact;
	public SimpleStringProperty mcop;
	public SimpleStringProperty firstHear;
	public SimpleStringProperty epNo;
	public SimpleStringProperty fir;
	public SimpleStringProperty dateOfWarrent;
	public SimpleStringProperty nature;
	public SimpleStringProperty punishment;
	public SimpleStringProperty dar;
	public SimpleStringProperty fip;
	public SimpleStringProperty branch;
	public SimpleStringProperty typeOfRoad;
	public SimpleStringProperty route;

	public CaseDetail(int id, String dateOfAcc, String time, String place, String drName, String crName, String vehNo,
			String gh, String policeStation, String petName, String mact, String mcop, String firstHear, String epNo,
			String fir, String dateOfWarrent, String nature, String punishment, String dar, String fip, String branch,
			String typeOfRoad, String route) {
		this.id = new SimpleIntegerProperty(id);
		this.dateOfAcc = new SimpleStringProperty(dateOfAcc);
		this.time = new SimpleStringProperty(time);
		this.place = new SimpleStringProperty(place);
		this.drName = new SimpleStringProperty(drName);
		this.crName = new SimpleStringProperty(crName);
		this.vehNo = new SimpleStringProperty(vehNo);
		this.gh = new SimpleStringProperty(gh);
		this.policeStation = new SimpleStringProperty(policeStation);
		this.petName = new SimpleStringProperty(petName);
		this.mact = new SimpleStringProperty(mact);
		this.mcop = new SimpleStringProperty(mcop);
		this.firstHear = new SimpleStringProperty(firstHear);
		this.epNo = new SimpleStringProperty(epNo);
		this.fir = new SimpleStringProperty(fir);
		this.dateOfWarrent = new SimpleStringProperty(dateOfWarrent);
		this.nature = new SimpleStringProperty(nature);
		this.punishment = new SimpleStringProperty(punishment);
		this.dar = new SimpleStringProperty(dar);
		this.fip = new SimpleStringProperty(fip);
		this.branch = new SimpleStringProperty(branch);
		this.typeOfRoad = new SimpleStringProperty(typeOfRoad);
		this.route = new SimpleStringProperty(route);
	}

	public int getId() {
		return id.get();
	}

	public String getDateOfAcc() {
		return dateOfAcc.get();
	}

	public String getTime() {
		return time.get();
	}

	public String getPlace() {
		return place.get();
	}

	public String getDrName() {
		return drName.get();
	}

	public String getCrName() {
		return crName.get();
	}

	public String getVehNo() {
		return vehNo.get();
	}

	public String getGh() {
		return gh.get();
	}

	public String getPoliceStation() {
		return policeStation.get();
	}

	public String getPetName() {
		return petName.get();
	}

	public String getMact() {
		return mact.get();
	}

	public String getMcop() {
		return mcop.get();
	}

	public String getFirstHear() {
		return firstHear.get();
	}

	public String getEpNo() {
		return epNo.get();
	}

	public String getFir() {
		return fir.get();
	}

	public String getDateOfWarrent() {
		return dateOfWarrent.get();
	}

	public String getNature() {
		return nature.get();
	}

	public String getPunishment() {
		return punishment.get();
	}

	public String getDar() {
		return dar.get();
	}

	public String getFip() {
		return fip.get();
	}

	public String getBranch() {
		return branch.get();
	}

	public String getTypeOfRoad() {
		return typeOfRoad.get();
	}

	public String getRoute() {
		return route.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public void setDateOfAcc(String dateOfAcc) {
		this.dateOfAcc.set(dateOfAcc);
	}

	public void setTime(String time) {
		this.time.set(time);
	}

	public void setPlace(String place) {
		this.place.set(place);
	}

	public void setDrName(String drName) {
		this.drName.set(drName);
	}

	public void setCrName(String crName) {
		this.crName.set(crName);
	}

	public void setVehNo(String vehNo) {
		this.vehNo.set(vehNo);
	}

	public void setGh(String gh) {
		this.gh.set(gh);
	}

	public void setPoliceStation(String policeStation) {
		this.policeStation.set(policeStation);
	}

	public void setPetName(String petName) {
		this.petName.set(petName);
	}

	public void setMact(String mact) {
		this.mact.set(mact);
	}

	public void setMcop(String mcop) {
		this.mcop.set(mcop);
	}

	public void setFirstHear(String firstHear) {
		this.firstHear.set(firstHear);
	}

	public void setEpNo(String epNo) {
		this.epNo.set(epNo);
	}

	public void setFir(String fir) {
		this.fir.set(fir);
	}

	public void setDateOfWarrent(String dateOfWarrent) {
		this.dateOfWarrent.set(dateOfWarrent);
	}

	public void setNature(String nature) {
		this.nature.set(nature);
	}

	public void setPunishment(String punishment) {
		this.punishment.set(punishment);
	}

	public void setDar(String dar) {
		this.dar.set(dar);
	}

	public void setFip(String fip) {
		this.fip.set(fip);
	}

	public void setBranch(String branch) {
		this.branch.set(branch);
	}

	public void setTypeOfRoad(String typeOfRoad) {
		this.typeOfRoad.set(typeOfRoad);
	}

	public void setRoute(String route) {
		this.route.set(route);
	}

}
