export function throwIfAlreadyLoaded(parentModule: any, moduleName: string)
{
    if (parentModule)
    {
        throw new Error (moduleName + ' has been already loaded');
    }
}
