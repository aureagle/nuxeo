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
 *     Antoine Taillefer
 */
package org.nuxeo.functionaltests.pages.forms;

import org.nuxeo.functionaltests.Required;
import org.nuxeo.functionaltests.pages.AbstractPage;
import org.nuxeo.functionaltests.pages.DocumentBasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Sun Seng David TAN <stan@nuxeo.com>
 */
public class WorkspaceFormPage extends AbstractPage {

    @Required
    @FindBy(id = "document_create:nxl_heading:nxw_title")
    WebElement titleTextInput;

    @Required
    @FindBy(id = "document_create:nxl_heading:nxw_description")
    WebElement descriptionTextInput;

    @Required
    @FindBy(id = "document_create:nxw_documentCreateButtons_CREATE_WORKSPACE")
    WebElement createButton;

    public WorkspaceFormPage(WebDriver driver) {
        super(driver);
    }

    public DocumentBasePage createNewWorkspace(String workspaceTitle, String workspaceDescription) {
        titleTextInput.sendKeys(workspaceTitle);
        descriptionTextInput.sendKeys(workspaceDescription);
        createButton.click();

        return asPage(DocumentBasePage.class);
    }

}
