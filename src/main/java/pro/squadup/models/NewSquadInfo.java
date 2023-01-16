package pro.squadup.models;

import java.util.ArrayList;

public class NewSquadInfo {

    private String name;
    private ArrayList<Long> inviteeIds;
    private Long squadPictureId;

    public NewSquadInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Long> getInviteeIds() {
        return inviteeIds;
    }

    public void setInviteeIds(ArrayList<Long> inviteeIds) {
        this.inviteeIds = inviteeIds;
    }

    public Long getSquadPictureId() {
        return squadPictureId;
    }

    public void setSquadPictureId(Long squadPictureId) {
        this.squadPictureId = squadPictureId;
    }
}
