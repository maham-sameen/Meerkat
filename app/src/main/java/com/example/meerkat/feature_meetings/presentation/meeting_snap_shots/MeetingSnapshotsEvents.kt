package com.example.meerkat.feature_meetings.presentation.meeting_snap_shots

import com.example.meerkat.feature_meetings.domain.util.MeetingSnapshot

sealed class MeetingSnapshotsEvents {
    data object  OnDismissSessionDialogState : MeetingSnapshotsEvents()
    data object  OnDismissCancelMeetingDialogState : MeetingSnapshotsEvents()
    data class  ShowCancelMeetingDialogState(val meetingSnapshot: MeetingSnapshot) : MeetingSnapshotsEvents()
    data object  ConfirmCancelMeeting : MeetingSnapshotsEvents()
}