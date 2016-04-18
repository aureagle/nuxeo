/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Sun Seng David TAN <stan@nuxeo.com>
 */
package org.nuxeo.functionaltests.pages.tabs;

import java.util.ArrayList;
import java.util.List;

import org.nuxeo.functionaltests.AjaxRequestManager;
import org.nuxeo.functionaltests.pages.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Sun Seng David TAN <stan@nuxeo.com>
 */
public class SummaryTabSubPage extends AbstractPage {

    private static final String COLLECTIONS_FORM_ID = "nxl_grid_summary_layout:nxw_summary_current_document_collections_form";

    public static final String WORKFLOW_START_BUTTON_XPATH = "//input[contains(@id, 'nxw_start_route_widget_start_route')]";

    public static final String WORKFLOW_SELECTOR_XPATH = "//select[contains(@id, 'nxw_start_route_widget')]";

    @FindBy(xpath = WORKFLOW_START_BUTTON_XPATH)
    public WebElement startWorkflowBtn;

    @FindBy(xpath = WORKFLOW_SELECTOR_XPATH)
    public WebElement workflowSelector;

    @FindBy(xpath = "//form[contains(@id, 'nxl_grid_summary_layout:nxw_summary_current_document_single_tasks')]")
    public WebElement workflowTasksForm;

    @FindBy(xpath = "//div[@class='nxw_lastContributor']")
    public WebElement lastContributor;

    @FindBy(xpath = "//div[@class='nxw_author']")
    public WebElement creator;

    @FindBy(xpath = "//span[@id='nxl_grid_summary_layout:nxw_summary_current_document_dublincore_form:nxl_dublincore:nxw_created']")
    public WebElement createdAt;

    @FindBy(xpath = "//span[@id='nxl_grid_summary_layout:nxw_summary_current_document_dublincore_form:nxl_dublincore:nxw_modified']")
    public WebElement lastModifiedAt;

    @FindBy(xpath = "//span[@class[starts-with(.,'nxw_contributors_')]]")
    public List<WebElement> contributors;

    @FindBy(xpath = "//form[@id='nxl_grid_summary_layout:nxw_summary_current_document_states_form']")
    public WebElement lifeCycleState;

    @FindBy(xpath = "//div[@class='publication_block']")
    public WebElement publicationBlock;

    public SummaryTabSubPage(WebDriver driver) {
        super(driver);
    }

    public void startDefaultWorkflow() {
        AjaxRequestManager a = new AjaxRequestManager(driver);
        a.watchAjaxRequests();
        selectItemInDropDownMenu(workflowSelector, "Serial document review");
        a.waitForAjaxRequests();
        startWorkflowBtn.click();
    }

    public void startDefaultParallelWorkflow() {
        selectItemInDropDownMenu(workflowSelector, "Parallel document review");

        startWorkflowBtn.click();
    }

    public boolean workflowAlreadyStarted() {
        return findElementWithTimeout(By.xpath("//*[@id='nxl_grid_summary_layout:nxw_summary_document_route_form']")).getText().contains(
                "review has been started");
    }

    public boolean openTaskForCurrentUser() {
        return findElementWithTimeout(
                By.xpath("//form[contains(@id, 'nxl_grid_summary_layout:nxw_summary_current_document_single_tasks')]")).getText().contains(
                "Please accept or reject the document");
    }

    /**
     * @since 5.8
     */
    public boolean parallelOpenTaskForCurrentUser() {
        return findElementWithTimeout(
                By.xpath("//form[contains(@id, 'nxl_grid_summary_layout:nxw_summary_current_document_single_tasks')]")).getText().contains(
                "Please give your opinion. Click on N/A if you have no advice.");
    }

    public WorkflowTabSubPage getWorkflow() {
        clickOnTabIfNotSelected("nxw_documentTabs_panel", "nxw_TAB_ROUTE_WORKFLOW");
        return asPage(WorkflowTabSubPage.class);
    }

    public boolean cantStartWorkflow() {
        return findElementWithTimeout(
                By.xpath("//form[contains(@id, 'nxl_grid_summary_layout:nxw_summary_document_route_form')]")).getText().contains(
                "No workflow process can be started on this document.");
    }

    /**
     * Get the creator of the doc.
     *
     * @since 5.8
     */
    public String getCreator() {
        return creator.getText();
    }

    /**
     * Get the last contributor of the doc.
     *
     * @since 5.8
     */
    public String getLastContributor() {
        return lastContributor.getText();
    }

    /**
     * Get the list of contributors of the doc.
     *
     * @since 5.8
     */
    public List<String> getContributors() {
        List<String> result = new ArrayList<String>();
        for (WebElement contributor : contributors) {
            result.add(contributor.getText());
        }
        return result;
    }

    /**
     * @since 5.8
     */
    public String getCurrentLifeCycleState() {
        return lifeCycleState.findElement(By.className("sticker")).getText();
    }

    /**
     * @since 5.9.3
     */
    public boolean isCollectionsFormDisplayed() {
        try {
            driver.findElement(By.id(COLLECTIONS_FORM_ID));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * @since 5.9.3
     */
    public int getCollectionCount() {
        return driver.findElement(By.id(COLLECTIONS_FORM_ID)).findElements(
                By.xpath("div/span[@id='nxl_grid_summary_layout:nxw_summary_current_document_collections_form:collections']/span[@class='tag tagLink']")).size();
    }

    /**
     * @since 8.2
     */
    public boolean isPublished() {
        return publicationBlock.getText().contains("This document is published.");
    }

    /**
     * @since 8.2
     */
    public boolean isAwaitingPublication() {
        return publicationBlock.getText().contains("This document is waiting for a publication approval.");

    }

    /**
     * @since 8.2
     */
    public SummaryTabSubPage approvePublication() {
        publicationBlock.findElement(By.xpath(".//input[@value='Approve']")).click();
        return asPage(SummaryTabSubPage.class);
    }

    /**
     * @since 8.2
     */
    public void rejectPublication(String comment) {
        WebElement text = publicationBlock.findElement(By.xpath(".//*[contains(@name, 'rejectPublishingComment')]"));
        text.sendKeys(comment);
        publicationBlock.findElement(By.xpath(".//input[@value='Reject']")).click();
    }
}
