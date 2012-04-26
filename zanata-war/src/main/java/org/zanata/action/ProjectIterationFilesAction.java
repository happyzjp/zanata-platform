/*
 * Copyright 2010, Red Hat, Inc. and individual contributors as indicated by the
 * @author tags. See the copyright.txt file in the distribution for a full
 * listing of individual contributors.
 * 
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.zanata.action;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.zanata.common.EntityStatus;
import org.zanata.common.LocaleId;
import org.zanata.common.MergeType;
import org.zanata.common.TransUnitWords;
import org.zanata.common.TranslationStats;
import org.zanata.dao.DocumentDAO;
import org.zanata.dao.LocaleDAO;
import org.zanata.dao.ProjectIterationDAO;
import org.zanata.exception.ZanataServiceException;
import org.zanata.model.HDocument;
import org.zanata.model.HLocale;
import org.zanata.rest.StringSet;
import org.zanata.rest.dto.extensions.ExtensionType;
import org.zanata.rest.dto.resource.TextFlowTarget;
import org.zanata.rest.dto.resource.TranslationsResource;
import org.zanata.service.TranslationFileService;
import org.zanata.service.TranslationService;

import java.util.Collection;
import java.util.List;

import static org.jboss.seam.international.StatusMessage.Severity;

@Name("projectIterationFilesAction")
@Scope(ScopeType.PAGE)
public class ProjectIterationFilesAction
{

   private String projectSlug;
   
   private String iterationSlug;

   private String localeId;
   
   @In
   private DocumentDAO documentDAO;
   
   @In
   private LocaleDAO localeDAO;

   @In
   private ProjectIterationDAO projectIterationDAO;

   @In
   private TranslationFileService translationFileServiceImpl;

   @In
   private TranslationService translationServiceImpl;

   private List<HDocument> iterationDocuments;
   
   private String documentNameFilter;

   private TranslationFileUploadHelper fileUploadHelper;
   
   
   public void initialize()
   {
      this.iterationDocuments = this.documentDAO.getAllByProjectIteration(this.projectSlug, this.iterationSlug);
      this.fileUploadHelper = new TranslationFileUploadHelper();
   }
   
   public HLocale getLocale()
   {
      return localeDAO.findByLocaleId(new LocaleId(localeId));
   }

   public boolean filterDocumentByName( Object docObject )
   {
      final HDocument document = (HDocument)docObject;
      
      if( this.documentNameFilter != null && this.documentNameFilter.length() > 0 )
      {
         return document.getName().toLowerCase().contains( this.documentNameFilter.toLowerCase() );
      }
      else
      {
         return true;
      }
   }
   
   public TransUnitWords getTransUnitWordsForDocument(HDocument doc)
   {
      TranslationStats documentStats = this.documentDAO.getStatistics(doc.getId(), new LocaleId(this.localeId));
      return documentStats.getWordCount();
   }

   public void uploadFile()
   {
      TranslationsResource transRes = null;
      try
      {
         // process the file
         transRes = this.translationFileServiceImpl.parseTranslationFile(this.fileUploadHelper.getFileContents(),
               this.fileUploadHelper.getFileName());

         // translate it
         Collection<TextFlowTarget> resourcesNotFound =
            this.translationServiceImpl.translateAll(this.projectSlug, this.iterationSlug, this.fileUploadHelper.getDocId(),
               new LocaleId(this.localeId), transRes, new StringSet(ExtensionType.GetText.toString()),
               this.fileUploadHelper.getMergeTranslations() ? MergeType.AUTO : MergeType.IMPORT);

         StringBuilder facesInfoMssg = new StringBuilder("File {0} uploaded.");
         if( resourcesNotFound.size() > 0 )
         {
            facesInfoMssg.append(" There were some warnings, see below.");
         }

         FacesMessages.instance().add(Severity.INFO, facesInfoMssg.toString(), this.fileUploadHelper.getFileName());
         for( TextFlowTarget nf : resourcesNotFound )
         {
            FacesMessages.instance().add(Severity.WARN, "Could not find text flow for message: {0}", nf.getContents());
         }
      }
      catch (ZanataServiceException zex)
      {
         FacesMessages.instance().add(Severity.ERROR, "Invalid file type: {0}", this.fileUploadHelper.getFileName());
      }
   }

   public boolean isFileUploadAllowed()
   {
      return this.projectIterationDAO.getBySlug(projectSlug, iterationSlug).getStatus() == EntityStatus.ACTIVE;
   }

   public List<HDocument> getIterationDocuments()
   {
      return iterationDocuments;
   }

   public void setIterationDocuments(List<HDocument> iterationDocuments)
   {
      this.iterationDocuments = iterationDocuments;
   }

   public String getProjectSlug()
   {
      return projectSlug;
   }

   public void setProjectSlug(String projectSlug)
   {
      this.projectSlug = projectSlug;
   }

   public String getIterationSlug()
   {
      return iterationSlug;
   }

   public void setIterationSlug(String iterationSlug)
   {
      this.iterationSlug = iterationSlug;
   }

   public String getLocaleId()
   {
      return localeId;
   }

   public void setLocaleId(String localeId)
   {
      this.localeId = localeId;
   }

   public String getDocumentNameFilter()
   {
      return documentNameFilter;
   }

   public void setDocumentNameFilter(String documentNameFilter)
   {
      this.documentNameFilter = documentNameFilter;
   }

   public TranslationFileUploadHelper getFileUploadHelper()
   {
      return fileUploadHelper;
   }
}
