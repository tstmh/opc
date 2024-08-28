package com.stee.spfcore.service.marketing;

import java.util.List;

import com.stee.spfcore.model.marketing.MemberGroup;
import com.stee.spfcore.model.personnel.PersonalDetail;
import com.stee.spfcore.vo.marketing.MemberGroupNamedValuePair;
import com.stee.spfcore.vo.marketing.MemberGroupSummaryList;
import com.stee.spfcore.vo.personnel.PersonalNricName;

public interface IMarketingService {

    public MemberGroup getMemberGroup( String id ) throws MarketingServiceException;

    public String addMemberGroup( MemberGroup group, String requester ) throws MarketingServiceException;

    public void updateMemberGroup( MemberGroup group, String requester ) throws MarketingServiceException;

    public int getMemberGroupCount( boolean template, boolean includeDisabled ) throws MarketingServiceException;

    public int getMemberGroupCount( String module, boolean template, boolean includeDisabled ) throws MarketingServiceException;

    public List< MemberGroupSummaryList > getMemberGroupsSummaryList( boolean template, boolean includeDisabled ) throws MarketingServiceException;

    public List< MemberGroup > getMemberGroups( boolean template, boolean includeDisabled ) throws MarketingServiceException;

    public List< MemberGroup > getMemberGroups( String module, boolean template, boolean includeDisabled ) throws MarketingServiceException;

    public List< MemberGroup > getMemberGroups( int pageNum, int pageSize, boolean template, boolean includeDisabled ) throws MarketingServiceException;

    public List< MemberGroup > getMemberGroups( int pageNum, int pageSize, String module, boolean template, boolean includeDisabled ) throws MarketingServiceException;

    public List< String > getMemberInGroup( String id ) throws MarketingServiceException;

    public List< PersonalDetail > getPersonnelInGroup( String id ) throws MarketingServiceException;

    public List< PersonalNricName > getPersonnelNricNameInGroup( String id ) throws MarketingServiceException;

    public List< String > getNonExistPersonnel( List< String > users ) throws MarketingServiceException;
    
    public List < MemberGroupNamedValuePair > getMemberGroupNVP (String module, boolean includeDisabled) throws MarketingServiceException;
    
    public List< MemberGroup > getMemberGroupsOfAnnouncement( String module, boolean template, List< String > announcementIds) throws MarketingServiceException;

	List<MemberGroup> getMemberGroupsByAnnoucementIds(boolean includeDisabled, List<String> announcementIds) throws MarketingServiceException;

	List<MemberGroup> getMemberGroupByIds(List<String> memberGroupIds, boolean includeDisabled) throws MarketingServiceException;
    
    
}
