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
 *     Sun Seng David TAN
 *     Florent Guillaume
 *     Antoine Taillefer
 */
package org.nuxeo.ftest.cap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;
import org.nuxeo.functionaltests.AbstractTest;
import org.nuxeo.functionaltests.Locator;
import org.nuxeo.functionaltests.pages.DocumentBasePage;
import org.nuxeo.functionaltests.pages.FileDocumentBasePage;
import org.nuxeo.functionaltests.pages.forms.FileCreationFormPage;
import org.nuxeo.functionaltests.pages.tabs.EditTabSubPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Test file upload in Nuxeo DM.
 */
public class ITFileUploadTest extends AbstractTest {

    @Test
    public void testFileUpload() throws Exception {

        // Login as Administrator
        DocumentBasePage defaultDomainPage = login();

        // Init repository with a test Workspace
        String wsTitle = ITFileUploadTest.class.getSimpleName() + "_WorkspaceTitle_" + new Date().getTime();
        DocumentBasePage testWorkspacePage = createWorkspace(defaultDomainPage, wsTitle, "");

        // Create a File with an uploaded blob
        String filePrefix = "NX-Webdriver-test-";
        FileDocumentBasePage fileDocumentBasePage = createFile(testWorkspacePage, "File title", "File description",
                true, filePrefix, ".txt", "Webdriver test file content.");

        // Check uploaded file name
        String uploadedFileName = fileDocumentBasePage.getFileSummaryTab().getMainContentFileText();
        assertTrue("Wrong uploaded file name '" + uploadedFileName + "', expected it to contain '" + filePrefix + "'",
                uploadedFileName.contains(filePrefix));

        // Check removal of file
        WebElement sumContent = Locator.findElementWithTimeout(By.xpath("//div[@class=\"content_block\"]"));
        assertNotNull(sumContent);
        String sumContentText = sumContent.getText();
        assertNotNull(sumContentText);
        assertFalse(sumContentText.contains("Drop files here"));
        assertTrue(sumContentText.contains(uploadedFileName));
        EditTabSubPage editPage = fileDocumentBasePage.getEditTab();
        WebElement deleteChoice = Locator.findElementWithTimeout(By.id("document_edit:nxl_file:nxw_file:nxw_file_file:choicedelete"));
        assertNotNull(deleteChoice);
        deleteChoice.click();
        fileDocumentBasePage = editPage.save().asPage(FileDocumentBasePage.class);
        sumContent = Locator.findElementWithTimeout(By.xpath("//div[@class=\"content_block\"]"));
        assertNotNull(sumContent);
        sumContentText = sumContent.getText();
        assertNotNull(sumContentText);
        assertTrue(sumContentText.contains("Drop files here"));
        assertFalse(sumContentText.contains(uploadedFileName));

        // Clean up repository
        deleteWorkspace(fileDocumentBasePage, wsTitle);

        // Logout
        logout();
    }

    /**
     * Non-regression test for NXP-15638
     *
     * @since 7.1
     */
    @Test
    public void testFileUploadOnValidationError() throws Exception {

        // Login as Administrator
        DocumentBasePage defaultDomainPage = login();

        // Init repository with a test Workspace
        String wsTitle = ITSearchTabTest.class.getSimpleName() + "_WorkspaceTitle_" + new Date().getTime();
        DocumentBasePage testWorkspacePage = createWorkspace(defaultDomainPage, wsTitle, "");

        // Create a File with an uploaded blob
        String filePrefix = "NX-Webdriver-test-";
        // do not fill the title: expect a validation error to occur
        FileCreationFormPage fileCreationFormPage = testWorkspacePage.getContentTab().getDocumentCreatePage("File",
                FileCreationFormPage.class);
        FileCreationFormPage creationPageAfterError = fileCreationFormPage.createFileDocumentWithoutTitle(filePrefix,
                ".txt", "Webdriver test file content.");

        // Check validation error
        assertEquals("Value is required.", creationPageAfterError.getTitleMessage());

        // Check file is still there and filename is present
        assertEquals("tempKeep", creationPageAfterError.getSelectedOption());
        String filename = creationPageAfterError.getSelectedFilename();
        assertNotNull(filename);
        assertTrue("Wrong uploaded file name '" + filename + "', expected it to contain '" + filePrefix + "'",
                filename.contains(filePrefix));

        creationPageAfterError.titleTextInput.sendKeys("File title");
        creationPageAfterError.create();
        FileDocumentBasePage fileDocumentBasePage = asPage(FileDocumentBasePage.class);
        // Check uploaded file name
        String uploadedFileName = fileDocumentBasePage.getFileSummaryTab().getMainContentFileText();
        assertTrue("Wrong uploaded file name '" + uploadedFileName + "', expected it to contain '" + filePrefix + "'",
                uploadedFileName.contains(filePrefix));

        // Clean up repository
        deleteWorkspace(fileDocumentBasePage, wsTitle);

        // Logout
        logout();
    }

}
