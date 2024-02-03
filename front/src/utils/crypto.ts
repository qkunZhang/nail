import CryptoJS from 'crypto-js'


export function encryptSHA3(unEncryptedText:string):string{
    const sha3result = CryptoJS.SHA3(unEncryptedText, { outputLength: 256 });
    const base64URL = CryptoJS.enc.Base64.stringify(CryptoJS.enc.Hex.parse(sha3result.toString()));
    return base64URL.replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}

export function encryptXOR(unEncryptedText: string,type:string): string {
    let keyXOR:string = type
    unEncryptedText = generateRandomLetter()+unEncryptedText+generateRandomLetter()
    let result:string = '';
    for (let i = 0; i < unEncryptedText.length; i++) {
        result += String.fromCharCode(unEncryptedText.charCodeAt(i) ^ keyXOR.charCodeAt(i % keyXOR.length));
    }
    return encodeBASE64URLSafe(result);
}

function encodeBASE64URLSafe(input: string): string {
    const base64Encoded = btoa(input);
    return base64Encoded.replace(/\+/g, '-').replace(/\//g, '_').replace(/=+$/, '');
}

function generateRandomLetter(): string {
    const alphabet = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_@#$%^&*+=';
    const randomIndex = Math.floor(Math.random() * alphabet.length);
    return alphabet[randomIndex];
  }
  

 


