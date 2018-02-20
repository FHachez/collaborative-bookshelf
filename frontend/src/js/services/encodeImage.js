
const encodeImage = (arrayBuffer, mimetype) => {
  const u8 = new Uint8Array(arrayBuffer);
  const b64encoded = btoa(String.fromCharCode.apply(null, u8));
  return `data:${mimetype};base64,${b64encoded}`;
};

export default encodeImage;
