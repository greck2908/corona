//////////////////////////////////////////////////////////////////////////////
//
// This file is part of the Corona game engine.
// For overview and more information on licensing please refer to README.md 
// Home page: https://github.com/coronalabs/corona
// Contact: support@coronalabs.com
//
//////////////////////////////////////////////////////////////////////////////

#import "AppBuildController.h"

// ----------------------------------------------------------------------------

@interface WebAppBuildController : AppBuildController
{
	@private
		IBOutlet NSPopUpButton* fBuildFor;
	
@protected
	IBOutlet NSButton *fUseStandartResources;
	IBOutlet NSButton *fCreateFBInstantArchive;
}

@end

// ----------------------------------------------------------------------------
