#import "QRCodeModule.h"
#import "RCTBridgeModule.h"
#import "RCTBridge.h"

@implementation QRCodeModule

RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

RCT_EXPORT_METHOD(
	base64Image:(NSString *)message
	dimension:(NSUInteger)dimension
	resolve:(RCTResponseSenderBlock)resolve
	reject:(RCTResponseSenderBlock)reject
) {
  // Your implementation here
    NSLog(@"asdf");
}

@end
