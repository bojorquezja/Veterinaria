package main.java.com.wonen.veterinaria.views.comun;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.Enumeration;

public class MascaraTextField extends PlainDocument {
    private int minLength = 0;
    private int maxLength = 0;
    private TipoMascaraTextField tipoMascara = TipoMascaraTextField.TODO;

    public MascaraTextField(String porDefecto, int minLength, int maxLength, TipoMascaraTextField tipoMascara) {
        super();
        this.minLength = (minLength > 0 ? minLength : 0);
        this.maxLength = (maxLength > 0 ? maxLength : 0);
        this.tipoMascara = (tipoMascara == null ? TipoMascaraTextField.TODO : tipoMascara);
        AttributeSet a = new AttributeSet() {
            @Override
            public int getAttributeCount() {
                return 0;
            }

            @Override
            public boolean isDefined(Object attrName) {
                return false;
            }

            @Override
            public boolean isEqual(AttributeSet attr) {
                return false;
            }

            @Override
            public AttributeSet copyAttributes() {
                return null;
            }

            @Override
            public Object getAttribute(Object key) {
                return null;
            }

            @Override
            public Enumeration<?> getAttributeNames() {
                return null;
            }

            @Override
            public boolean containsAttribute(Object name, Object value) {
                return false;
            }

            @Override
            public boolean containsAttributes(AttributeSet attributes) {
                return false;
            }

            @Override
            public AttributeSet getResolveParent() {
                return null;
            }
        };
        if ((this.minLength > 0 && porDefecto.length()>= this.minLength) || this.minLength == 0) {
            if ((this.maxLength > 0 && porDefecto.length()<= this.maxLength) || this.maxLength == 0 ) {
                try {
                    super.insertString(0, porDefecto, a);
                } catch (BadLocationException e) {
                    //no hay valor inicial
                }
            }
        }
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null)
            return;
        if ((getLength() + str.length()) <= maxLength || maxLength == 0) {
            switch(tipoMascara){
                case SOLO_NUMEROS_ENTEROS:
                    if (this.soloTieneCaracteresCorrectos(str,"0123456789")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case SOLO_NUMEROS_DECIMALES:
                    if (this.soloTieneCaracteresCorrectos(str,"0123456789.")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case SOLO_LETRAS:
                    if (this.soloTieneCaracteresCorrectos(str,"abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case SOLO_LETRAS_MAYUSC:
                    if (this.soloTieneCaracteresCorrectos(str,"ABCDEFGHIJKLMNÑOPQRSTUVWXYZ")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case SOLO_LETRAS_MINUSC:
                    if (this.soloTieneCaracteresCorrectos(str,"abcdefghijklmnñopqrstuvwxyz")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case NUMEROS_Y_LETRAS:
                    if (this.soloTieneCaracteresCorrectos(str,"0123456789abcdefghijklmnñopqrstuvwxyzABCDEFGHIJKLMNÑOPQRSTUVWXYZ")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case NUMEROS_Y_LETRAS_MAYUSC:
                    if (this.soloTieneCaracteresCorrectos(str,"0123456789ABCDEFGHIJKLMNÑOPQRSTUVWXYZ")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case NUMEROS_Y_LETRAS_MINUSC:
                    if (this.soloTieneCaracteresCorrectos(str,"0123456789abcdefghijklmnñopqrstuvwxyz")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case CORREO:
                    if (this.soloTieneCaracteresCorrectos(str,"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.@")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case TELEFONO:
                    if (this.soloTieneCaracteresCorrectos(str,"0123456789#*")){
                        super.insertString(offs, str, a);
                    }
                    break;
                case TODO:
                    super.insertString(offs, str, a);
            }
        }
    }

    private boolean soloTieneCaracteresCorrectos(String textoAEvaluar, String charCorrectos){
        for (int x = 0 ; x< textoAEvaluar.length(); x++) {
            if (charCorrectos.indexOf(textoAEvaluar.charAt(x)) == -1){
                return false;
            }
        }
        return true;
    }

}
