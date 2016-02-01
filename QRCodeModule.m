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
	@try {
        CIContext *context = [CIContext contextWithOptions:nil];
	    NSDictionary *dic = @{ @"inputMessage" : [message dataUsingEncoding:NSUTF8StringEncoding] };
	    CIFilter *qrFilter = [CIFilter filterWithName:@"CIQRCodeGenerator" withInputParameters:dic];
        CIImage *qrImage = qrFilter.outputImage;
        CGFloat scale = dimension / qrImage.extent.size.width;
        CGAffineTransform t = CGAffineTransformMakeScale(scale, scale);
        CIFilter *affineFilter = [CIFilter filterWithName:@"CIAffineTransform"
                                            keysAndValues:@"inputImage", qrImage, nil];
        [affineFilter setValue:[NSValue valueWithBytes:&t
                                              objCType:@encode(CGAffineTransform)]
                        forKey:@"inputTransform"];
        CIImage *affineImage = affineFilter.outputImage;
        CGImageRef cgImage=[context createCGImage:affineImage fromRect:CGRectMake(0, 0, dimension, dimension)];
        UIImage *image = [UIImage imageWithCGImage:cgImage];
        NSData *data = UIImagePNGRepresentation(image);
		NSString *base64Image = [data base64EncodedStringWithOptions:NSDataBase64Encoding64CharacterLineLength];
		resolve(@[base64Image]);
    } @catch (NSException *exception) {
        reject(@[[NSString stringWithFormat:@"%@", exception.reason]]);
    }
}

@end
