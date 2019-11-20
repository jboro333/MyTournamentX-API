package cat.udl.eps.softarch.mytournamentx.steps;

import cat.udl.eps.softarch.mytournamentx.domain.Team;
import cat.udl.eps.softarch.mytournamentx.domain.TeamInvitation;
import cat.udl.eps.softarch.mytournamentx.repository.TeamRepository;
import cucumber.api.PendingException;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import gherkin.ast.Step;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.print.attribute.standard.Media;

import static cat.udl.eps.softarch.mytournamentx.steps.AuthenticationStepDefs.authenticate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class JoinToTeamStepDefs {

    @Autowired
    private TeamRepository teamRepository;

    private Team team;

    @Autowired
    private StepDefs stepDefs;

    private TeamInvitation teamInv;

    @When("^I try to join a not created team with name \"([^\"]*)\"$")
    public void iTryToJoinTeamWithName(String name) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/teams/{name}",name)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @And("^I cannot join the team with name \"([^\"]*)\"$")
    public void iCannotJoinTheTeamWithName(String name) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/teams/{name}",name)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @And("^I can join the team with name \"([^\"]*)\"$")
    public void iCanJoinTheTeamWithName(String name) throws Throwable {
        team = teamRepository.findTeamByName(name);
        stepDefs.result = stepDefs.mockMvc.perform(
                post("/joinTeam")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .with(authenticate()))
                .andDo(print());
    }

    @When("^I try to join a created team with name \"([^\"]*)\"$")
    public void iTryToJoinACreatedTeamWithName(String name) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/teams/{name}",name)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @And("^I don't have invitation to team \"([^\"]*)\"$")
    public void iDonTHaveInvitationToTeam(String name) throws Throwable {
        stepDefs.result = stepDefs.mockMvc.perform(
                get("/teams/{name}",name)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @And("^I have been invited to a team with name \"([^\"]*)\" and message \"([^\"]*)\"$")
    public void iHaveBeenInvitedToATeamWithNameAndMessage(String name, String message) throws Throwable {
        TeamInvitation teamInvitation = new TeamInvitation();
        teamInvitation.setId("name");
        teamInvitation.setMessage("message");
        teamRepository.save(teamInvitation);
    }
}
